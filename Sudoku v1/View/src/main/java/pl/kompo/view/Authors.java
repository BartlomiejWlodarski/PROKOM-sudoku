package pl.kompo.view;

import java.util.ListResourceBundle;

public class Authors extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"a1", "Adam Stefański "},
                {"a2", "Bartłomiej Włodarski "}
        };
    }
}
