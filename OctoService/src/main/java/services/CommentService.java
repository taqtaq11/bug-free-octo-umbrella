package services;

import models.Comment;
import models.Mesh;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Alexander on 07/05/16.
 */

@Stateless
public class CommentService {

    @Inject
    ConnectionService connectionService;

    public Comment read(long id) throws Exception {
        Connection connection = connectionService.getConnection();

        // General info
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM public.comment WHERE id = ?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("User doen't exist.");
        }

        Comment comment = new Comment(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("mesh_id"),
                rs.getString("message"),
                rs.getTimestamp("timestamp")
        );

        rs.close();
        ps.close();
        connection.close();

        return comment;
    }

}
