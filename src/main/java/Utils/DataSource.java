package Utils;
import java.sql.*;
public class DataSource {
    private  String url="jdbc:mysql://localhost:3306/pi_dev_db";
    private  String login="root";
    private  String pwd="";
    private static DataSource data;

    private Connection con;


    private DataSource(){
        try {
            con=DriverManager.getConnection(url,login,pwd);
            System.out.println("connexion établie");
        } catch (SQLException e) {

            System.out.println(e);
        }

    }
    public Connection getCon() {
        return con;
    }

    public static DataSource getInstance() {

        if(data==null)
        {
            data=new DataSource();
        }
        return data;
    }
}
