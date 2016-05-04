package services;

import javax.ejb.Stateless;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Alexander on 20/03/16.
 */

@Stateless
public class ConnectionService {
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://hiterminal.cf:5432/octobase", "octouser", "octopass");
    }
}
