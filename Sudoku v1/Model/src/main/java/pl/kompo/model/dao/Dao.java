package pl.kompo.model.dao;

public interface Dao<T> extends AutoCloseable {

    T read();

    void write(T obj);
}
