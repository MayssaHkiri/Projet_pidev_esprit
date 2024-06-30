package Services;

import Entities.Matiere;

import java.sql.SQLException;
import java.util.List;
import Utils.DataSource;
import java.sql.*;
import java.util.ArrayList;


public class ServiceMatiere implements IService<Matiere>{


    private Connection conn = DataSource.getInstance().getCon();
    private Statement ste;

    public ServiceMatiere(){

        try {
            ste=conn.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public List<Matiere> readAll() throws SQLException {
        List<Matiere> matieres = new ArrayList<>();
        String sql = "SELECT * FROM matiere";


             ResultSet rs = ste.executeQuery(sql);


            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                int coeff = rs.getInt("coeff");
                String modeEval = rs.getString("ModeEval");
                matieres.add(new Matiere(id, nom, coeff, modeEval));
            }


        return matieres;
    }

    public void ajouter(Matiere matiere) throws SQLException {
        String req="INSERT INTO `matiere` ( `nom`, `coeff`, `ModeEval`) VALUES ( '"+matiere.getNom()+"', '"+matiere.getCoeff()+"', '"+matiere.getModeEval()+"');";
        ste.executeUpdate(req);
    }

    public void update(Matiere matiere) throws SQLException {
        String req = "UPDATE `matiere` SET `nom` = '"
                + matiere.getNom() + "', `coeff` = '"
                + matiere.getCoeff() + "', `ModeEval` = '"
                + matiere.getModeEval() + "' WHERE `id` = "
                + matiere.getId() + ";";
        ste.executeUpdate(req);
    }

    @Override
    public Matiere findbyId(int e) throws SQLException {
        return null;
    }

    public void supprimer(Matiere matiere) throws SQLException {
        String req = "DELETE FROM `matiere` WHERE `id` = " + matiere.getId()+ ";";
        ste.executeUpdate(req);
    }
}
