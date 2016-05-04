package services;

import models.User;
import models.UserShort;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.*;
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

    public UserShort readShort(long id) throws SQLException {
        Connection connection = connectionService.getConnection();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM public.user WHERE id = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();

        UserShort userShort = new UserShort(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("avatar_file")
                );

        ps = connection.prepareStatement("SELECT * FROM public.follower WHERE follower_id = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setLong(1, id);
        rs = ps.executeQuery();
        rs.last();
        userShort.setSubscriptionsCount(rs.getRow());

        ps = connection.prepareStatement("SELECT * FROM public.follower WHERE user_id = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setLong(1, id);
        rs = ps.executeQuery();
        rs.last();
        userShort.setFollowersCount(rs.getRow());

        ps = connection.prepareStatement("SELECT * FROM public.mesh WHERE author = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setLong(1, id);
        rs = ps.executeQuery();
        rs.last();
        userShort.setMeshesCount(rs.getRow());

        ps = connection.prepareStatement("SELECT * FROM public.like WHERE user_id = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setLong(1, id);
        rs = ps.executeQuery();
        rs.last();
        userShort.setLikesCount(rs.getRow());

        return userShort;
    }

// TODO: 20/03/16 implement
//    public User read(long id) {}
//    public void update(String token, long id, String newName, String oldPass, String newPass, String newAvatarFile) {}
//    public List<User> search(String query) {}
//    public void follow(String token, long id, long toFollowId) {}
//    public void unfollow(String token, long id, long toUnfollowId) {}
}
