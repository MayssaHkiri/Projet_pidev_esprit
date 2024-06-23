package StartApplication;


import Utils.DatabaseUpdater;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseUpdater updater = new DatabaseUpdater();
        updater.updateDatabase();
    }
}