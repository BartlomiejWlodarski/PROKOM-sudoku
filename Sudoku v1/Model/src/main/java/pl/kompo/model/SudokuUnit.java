package pl.kompo.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.kompo.model.exceptions.CloneException;

public abstract class SudokuUnit implements Cloneable {

    private List<SudokuField> fields = Arrays.asList(new SudokuField[9]);
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ModelLanguages");

    public SudokuUnit(List<SudokuField> newFields) {
        Objects.requireNonNull(newFields);
        if (newFields.size() == 9) {
            fields = newFields;
        }
    }

    public boolean verify() {
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (fields.get(i).getFieldValue() == fields.get(j).getFieldValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<SudokuField> getFields() {
        return fields;
    }

    public void setFields(List<SudokuField> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuUnit that = (SudokuUnit) o;

        return new EqualsBuilder().append(fields, that.fields).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(fields).append(this.getClass()).toHashCode();
    }

    @Override
    public String toString() {
        return fields.toString();
    }

    @Override
    public Object clone() throws CloneException {
        List<SudokuField> cloneFields = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            cloneFields.set(i, fields.get(i));
        }
        try {
            return this.getClass().getDeclaredConstructor(List.class).newInstance(cloneFields);
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {
            throw new CloneException(this.getClass().toString()
                    + resourceBundle.getString("clone_exception"), e);
        }
    }
}
