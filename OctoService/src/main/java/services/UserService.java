package services;

import models.Session;
import models.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander on 20/03/16.
 */

@Stateless
public class UserService {
    @Inject
    ConnectionService connectionService;

    @Inject
    HashService hashService;

    @Inject
    SessionService sessionService;

    public void create(String login, String password, String name) throws SQLException {
        Connection connection = connectionService.getConnection();

        PreparedStatement ps =
                connection.prepareStatement("INSERT INTO public.user (login, password, name) VALUES (?, ?, ?)");

        ps.setString(1, login);
        ps.setString(2, hashService.hash(password));
        ps.setString(3, name);
        ps.execute();

        ps.close();
        connection.close();
    }

    public User read(long id) throws Exception {
        Connection connection = connectionService.getConnection();

        // General info
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM public.user WHERE id = ?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("User doen't exist.");
        }

        User user = new User(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("avatar_file")
                );

        rs.close();
        ps.close();

        // Meshes
        ps = connection.prepareStatement("SELECT * FROM public.mesh WHERE author = ?");
        ps.setLong(1, id);
        rs = ps.executeQuery();

        while (rs.next()) {
            user.getMeshes().add(rs.getLong("id"));
        }

        rs.close();
        ps.close();

        // Subscriptions
        ps = connection.prepareStatement("SELECT * FROM public.follower WHERE follower_id = ?");
        ps.setLong(1, id);
        rs = ps.executeQuery();

        while (rs.next()) {
            user.getSubscriptions().add(rs.getLong("user_id"));
        }

        rs.close();
        ps.close();

        // Followers
        ps = connection.prepareStatement("SELECT * FROM public.follower WHERE user_id = ?");
        ps.setLong(1, id);
        rs = ps.executeQuery();

        while (rs.next()) {
            user.getFollowers().add(rs.getLong("follower_id"));
        }

        rs.close();
        ps.close();

        // Likes
        ps = connection.prepareStatement("SELECT * FROM public.like WHERE user_id = ?");
        ps.setLong(1, id);
        rs = ps.executeQuery();

        while (rs.next()) {
            user.getLikes().add(rs.getLong("mesh_id"));
        }

        rs.close();
        ps.close();

        // Comments
        ps = connection.prepareStatement("SELECT * FROM public.comment WHERE user_id = ?");
        ps.setLong(1, id);
        rs = ps.executeQuery();

        while (rs.next()) {
            user.getComments().add(rs.getLong("id"));
        }

        rs.close();
        ps.close();
        connection.close();

        return user;
    }

    // TODO: 06/05/16 realize
//    public void update(String token, long id, String newName, String oldPass, String newPass, String newAvatarFile) {}

    public List<User> search(String query) throws Exception {
        List<User> users = new LinkedList<>();

        Connection connection = connectionService.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM public.user");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            users.add(read(rs.getLong("id")));
        }

        rs.close();
        ps.close();

        return users;
    }

    public void follow(String token, String ip, long id, long toFollowId) throws Exception {
        long userId = sessionService.check(token, ip);

        if (userId != id) {
            throw new Exception("You can only follow using your id.");
        }

        Connection connection = connectionService.getConnection();
        PreparedStatement ps =
                connection.prepareStatement("INSERT INTO public.follower(follower_id, user_id) VALUES (?, ?)");
        ps.setLong(1, userId);
        ps.setLong(2, toFollowId);
        ps.execute();
        ps.close();
        connection.close();
    }

    public void unfollow(String token, String ip, long id, long toUnfollowId) throws Exception {
        long userId = sessionService.check(token, ip);

        if (userId != id) {
            throw new Exception("You can only unfollow using your id.");
        }

        Connection connection = connectionService.getConnection();
        PreparedStatement ps =
                connection.prepareStatement("DELETE FROM public.follower WHERE follower_id = ? AND user_id = ?");
        ps.setLong(1, userId);
        ps.setLong(2, toUnfollowId);
        ps.execute();
        ps.close();
        connection.close();
    }
}
