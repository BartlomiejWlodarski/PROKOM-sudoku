package pl.kompo.model.dao;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kompo.model.SudokuBoard;
import pl.kompo.model.exceptions.DaoException;
import pl.kompo.model.exceptions.FileException;


public class FileSudokuBoardDao implements Dao<SudokuBoard> {

    private final Path file;
    final Logger logger = LoggerFactory.getLogger(FileSudokuBoardDao.class);
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ModelLanguages");


    public FileSudokuBoardDao(Path file) {
        this.file = file;
    }

    @Override
    public SudokuBoard read() throws DaoException {
        SudokuBoard obj;
        try (FileInputStream fis = new FileInputStream(file.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            obj = (SudokuBoard) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new FileException(resourceBundle.getString("file_exception_read"), e);
        }

        return obj;
    }

    @Override
    public void write(SudokuBoard obj) throws FileException {
        try (FileOutputStream fos = new FileOutputStream(file.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new FileException(resourceBundle.getString("file_exception_write"), e);
        }

    }

    @Override
    public void close() {
        logger.info(resourceBundle.getString("resources_closed"));
    }
}
