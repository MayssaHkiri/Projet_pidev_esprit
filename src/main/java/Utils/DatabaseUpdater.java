package Utils;

import java.sql.*;

public class DatabaseUpdater {
    private Connection con;

    public DatabaseUpdater() {
        this.con = DataSource.getInstance().getCon();
    }

    public void updateDatabase() throws SQLException {
        try (Statement stmt = con.createStatement()) {
            // Temporarily disable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS=0;");

            // Drop the foreign key constraints
            stmt.execute("ALTER TABLE reponse DROP FOREIGN KEY IF EXISTS fk_reponse_choixpossible;");
            stmt.execute("ALTER TABLE reponse DROP FOREIGN KEY IF EXISTS fk_reponse_question;");
            stmt.execute("ALTER TABLE choixpossible DROP FOREIGN KEY IF EXISTS fk_choixpossible_question;");

            // Alter the id column
            stmt.execute("ALTER TABLE choixpossible MODIFY id INT AUTO_INCREMENT;");

            // Re-add the foreign key constraints
            stmt.execute("ALTER TABLE reponse ADD CONSTRAINT fk_reponse_choixpossible FOREIGN KEY (choixPossibleId) REFERENCES choixpossible(id);");
            stmt.execute("ALTER TABLE reponse ADD CONSTRAINT fk_reponse_question FOREIGN KEY (questionId) REFERENCES question(id);");
            stmt.execute("ALTER TABLE choixpossible ADD CONSTRAINT fk_choixpossible_question FOREIGN KEY (questionId) REFERENCES question(id);");

            // Enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS=1;");
        } catch (SQLException e) {
            System.out.println("Error while updating the database schema: " + e.getMessage());
            throw e;
        }
    }
}