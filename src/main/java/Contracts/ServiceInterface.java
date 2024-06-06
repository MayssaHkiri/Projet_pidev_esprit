package Contracts;

import java.sql.SQLException;
import java.util.List;

public interface ServiceInterface<T> {
    void ajouter(T t) throws SQLException;
    void supprimer(T t) throws SQLException;
    void update(T t) throws SQLException;
    T findById(int id) throws SQLException;
    List<T> readAll() throws SQLException;

}
