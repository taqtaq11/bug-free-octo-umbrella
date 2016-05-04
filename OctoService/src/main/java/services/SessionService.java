package services;

import models.SessionData;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.*;
import java.util.Random;

/**
 * Created by Alexander on 20/03/16.
 */

@Stateless
public class SessionService {
    @Inject
    ConnectionService connectionService;

    @Inject
    HashService hashService;

    public SessionData create(String login, String password, String ip) throws Exception {
        Connection connection = connectionService.getConnection();

        // Check credentials
        PreparedStatement ps = connection
                .prepareStatement("SELECT * FROM public.user WHERE login = ? and password = ?");
        ps.setString(1, login);
        ps.setString(2, hashService.hash(password));

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Bad credentials.");
        }

        // Create session
        long userId = rs.getLong("id");
        String token = hashService.hash("" + new Random().nextLong());

        ps = connection.prepareStatement("INSERT INTO public.session (user_id, ip, token) VALUES (?, ?, ?)");
        ps.setLong(1, userId);
        ps.setString(2, ip);
        ps.setString(3, token);
        ps.execute();

        ps.close();
        connection.close();

        return new SessionData(userId, token);
    }

//    public void close(String token, String ip) {}
//    public long check(String check, String ip) {}
}
