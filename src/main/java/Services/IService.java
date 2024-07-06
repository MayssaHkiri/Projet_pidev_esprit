package Services;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IService<T>{

    ArrayList<T> getAll() throws SQLException;
    boolean add(T t) throws SQLException;
    boolean delete(int id) throws SQLException;
    boolean update(T t) throws SQLException;
    T getUserById (int id ) throws SQLException ;
}