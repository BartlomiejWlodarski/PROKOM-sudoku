package pl.kompo.view;

import javafx.util.StringConverter;

public class SudokuStringConverter extends StringConverter<Integer> {
    @Override
    public String toString(Integer t) {
        if (t == 0) {
            return "";
        }
        return t.toString();
    }

    @Override
    public Integer fromString(String string) {
        if (string.matches("[1-9]")) {
            return Integer.valueOf(string);
        }
        else {
            return 0;
        }
    }
}