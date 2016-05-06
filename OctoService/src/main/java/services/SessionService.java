package services;

import models.Session;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alexander on 20/03/16.
 */

@Stateless
public class SessionService {
    @Inject
    ConnectionService connectionService;

    @Inject
    HashService hashService;

    public final int SESSION_LIFE_TIME = 10 * 60 * 1000; // In ms(10 minutes)

    public Session create(String login, String password, String ip) throws Exception {
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

        ps = connection.prepareStatement("INSERT INTO public.session (user_id, ip, token, last_access) VALUES (?, ?, ?, ?)");
        ps.setLong(1, userId);
        ps.setString(2, ip);
        ps.setString(3, token);
        ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        ps.execute();

        ps.close();
        connection.close();

        return new Session(userId, token);
    }

    public void close(String token, String ip) throws SQLException {
        Connection connection = connectionService.getConnection();
        PreparedStatement ps =
                connection.prepareStatement("UPDATE public.session SET is_open = FALSE WHERE token = ? AND ip = ?");
        ps.setString(1, token);
        ps.setString(2, ip);
        ps.execute();
    }

    public long check(String token, String ip) throws Exception {
        // Check if session exists
        Connection connection = connectionService.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM public.session WHERE token = ? AND ip = ?");
        ps.setString(1, token);
        ps.setString(2, ip);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Session doesn't exist");
        }

        long curTime = System.currentTimeMillis();
        long sesTime = rs.getTimestamp("last_access").getTime();
        long dt = curTime - sesTime;
        boolean isSessionExpired = curTime - sesTime > SESSION_LIFE_TIME;

        if (!rs.getBoolean("is_open") || isSessionExpired) {
            throw new Exception("Session already closed");
        }

        long sessionId = rs.getLong("id");
        long userId = rs.getLong("user_id");

        rs.close();
        ps.close();

        // Update session
        ps = connection.prepareStatement("UPDATE public.session SET last_access = ? WHERE id = ?");
        ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        ps.setLong(2, sessionId);
        ps.execute();
        ps.close();

        return userId;
    }
}
