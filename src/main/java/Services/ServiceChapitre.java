package Services;

import Entities.Chapitre;
import Utils.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ServiceChapitre implements IService<Chapitre>{
    private Connection con1 = DataSource.getInstance().getCon();
    private Statement ste;
    @Override
    public void ajouter(Chapitre chapitre) throws SQLException {

    }

    @Override
    public void supprimer(Chapitre chapitre) throws SQLException {

    }

    @Override
    public void update(Chapitre chapitre) throws SQLException {

    }

    @Override
    public Chapitre findbyId(int e) throws SQLException {
        return null;
    }

    @Override
    public List<Chapitre> readAll() throws SQLException {
        return List.of();
    }
}
