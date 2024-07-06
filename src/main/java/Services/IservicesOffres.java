package Services;

import java.sql.SQLException;
import java.util.List;

public interface IservicesOffres <T> {
    void ajouter(T t) throws SQLException;
    void supprimer(T t) throws SQLException;
    void supprimerAll() throws SQLException;
    T findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T t) throws SQLException;
}
