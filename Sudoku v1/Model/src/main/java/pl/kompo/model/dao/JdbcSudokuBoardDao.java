package pl.kompo.model.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kompo.model.BacktrackingSudokuSolver;
import pl.kompo.model.SudokuBoard;
import pl.kompo.model.exceptions.DaoException;
import pl.kompo.model.exceptions.DbException;
import pl.kompo.model.exceptions.NameInDbException;


public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {

    private final String boardName;
    //private final String url = "jdbc:postgresql://localhost/SudokuBoardDB";
    private final String url = "jdbc:sqlite:SudokuBoardDB.db";
    private final Logger logger = LoggerFactory.getLogger(JdbcSudokuBoardDao.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("ModelLanguages");

    public JdbcSudokuBoardDao(String name) {
        boardName = name;
    }

    @Override
    public SudokuBoard read() {
        String selectBoardId = "SELECT board_id FROM sudoku_board WHERE board_name = ?";
        String selectBoardFields = "SELECT value, x, y FROM board_field WHERE board_id = ?";
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());

        try (Connection conn = DriverManager.getConnection(url, "postgres", "root")) {
            //  Class.forName("org.postgresql.Driver");
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement selectBoardIdSt = conn.prepareStatement(selectBoardId);
                 PreparedStatement selectBoardFieldsSt = conn.prepareStatement(selectBoardFields)) {
                selectBoardIdSt.setString(1, boardName);
                ResultSet boardId = selectBoardIdSt.executeQuery();
                if (!boardId.next()) {
                    throw new DbException(resourceBundle.getString("no_id_fail"));
                }
                selectBoardFieldsSt.setInt(1, boardId.getInt(1));
                ResultSet boardFields = selectBoardFieldsSt.executeQuery();

                while (boardFields.next()) {
                    sudokuBoard.set(boardFields.getInt(2), boardFields.getInt(3),
                            boardFields.getInt(1));
                }

                boardId.close();
                boardFields.close();
                conn.commit();
            } catch (SQLException e) {
                throw new DbException(e);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
        logger.debug(resourceBundle.getString("board")
                + boardName + resourceBundle.getString("read_board")
                + "\n" + sudokuBoard.toString());
        return sudokuBoard;
    }

    @Override
    public void write(SudokuBoard obj) throws DaoException {
        String createTableSudokuBoard = """
                CREATE TABLE IF NOT EXISTS sudoku_board (
                board_id INTEGER PRIMARY KEY AUTOINCREMENT,
                board_name VARCHAR(30) UNIQUE NOT NULL
                );""";

        String createTableBoardField =
                """
                CREATE TABLE IF NOT EXISTS board_field (
                field_id INTEGER PRIMARY KEY AUTOINCREMENT,
                board_id INTEGER NOT NULL,
                value INTEGER NOT NULL,
                x INTEGER NOT NULL CHECK (x >= 0 AND x <= 8),
                y INTEGER NOT NULL CHECK (y >= 0 AND y <= 8),
                FOREIGN KEY (board_id) REFERENCES sudoku_board (board_id)
                );""";

        String insertValuesBoard = "INSERT INTO sudoku_board (board_name) VALUES (?)";
        String insertValuesField = """
                INSERT INTO board_field (board_id, value, x, y)
                VALUES (?, ?, ?, ?)""";
        String selectBoardId = "SELECT board_id FROM sudoku_board WHERE board_name = ?";

        try (Connection conn = DriverManager.getConnection(url, "postgres", "root")) {
            try (Statement st = conn.createStatement()) {
                st.execute(createTableSudokuBoard);
                st.execute(createTableBoardField);

            } catch (SQLException e) {
                throw new DbException(e);
            }

            try (PreparedStatement insertBoardSt = conn.prepareStatement(insertValuesBoard);
                 PreparedStatement insertFieldSt = conn.prepareStatement(insertValuesField);
                 PreparedStatement selectBoardIdSt = conn.prepareStatement(selectBoardId)) {
                conn.setAutoCommit(false);

                insertBoardSt.setString(1, boardName);
                insertBoardSt.executeUpdate();

                selectBoardIdSt.setString(1, boardName);
                ResultSet id = selectBoardIdSt.executeQuery();
                if (!id.next()) {
                    throw new DbException(resourceBundle.getString("no_id_fail"));
                }
                System.out.println(id.getInt(1));

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        insertFieldSt.setInt(1, id.getInt(1));
                        insertFieldSt.setInt(2, obj.get(i, j));
                        insertFieldSt.setInt(3, i);
                        insertFieldSt.setInt(4, j);
                        insertFieldSt.executeUpdate();
                    }
                }
                id.close();
                conn.commit();
            } catch (SQLException e) {
                try {
                    logger.error(resourceBundle.getString("transaction_rollback"));
                    conn.rollback();
                } catch (SQLException excep) {
                    throw new DbException(excep);
                }
                throw new DbException(e);
            } catch (NameInDbException e) {
                throw new NameInDbException(e);
            }
        } catch (SQLException | NameInDbException e) {
            throw new DbException(e);
        }

    }

    @Override
    public void close() throws Exception {
    }
}
