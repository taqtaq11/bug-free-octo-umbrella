package services;

import models.Mesh;
import models.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 20/03/16.
 */

@Stateless
public class MeshService {
    @Inject
    ConnectionService connectionService;

    @Inject
    SessionService sessionService;

    public void create(String token, String ip, String name, String description, String objFile, String textureFile)
            throws Exception {
        long userId = sessionService.check(token, ip);

        Connection connection = connectionService.getConnection();

        PreparedStatement ps =
                connection.prepareStatement("INSERT INTO public.mesh (name, description, author, obj_file, texture_file, faces_count, vertices_count) VALUES (?, ?, ?, ?, ?, ?, ?)");

        ps.setString(1, name);
        ps.setString(2, description);
        ps.setLong(3, userId);
        ps.setString(4, objFile);
        ps.setString(5, textureFile);
        ps.setLong(6, (new Random()).nextInt(1000));    // TODO: 06/05/16 count faces, vertices
        ps.setLong(7, (new Random()).nextInt(1000));
        ps.execute();

        ps.close();
        connection.close();
    }

    public Mesh read(long id) throws Exception {
        Connection connection = connectionService.getConnection();

        // General info
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM public.mesh WHERE id = ?");
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("User doen't exist.");
        }

        Mesh mesh = new Mesh(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getLong("author"),
                rs.getString("obj_file"),
                rs.getString("texture_file"),
                rs.getLong("views_count"),
                rs.getLong("faces_count"),
                rs.getLong("vertices_count")
        );

        rs.close();
        ps.close();

        // Likes
        ps = connection.prepareStatement("SELECT * FROM public.like WHERE mesh_id = ?");
        ps.setLong(1, id);
        rs = ps.executeQuery();

        while (rs.next()) {
            mesh.getLikes().add(rs.getLong("user_id"));
        }

        rs.close();
        ps.close();

        // Comments
        ps = connection.prepareStatement("SELECT * FROM public.comment WHERE mesh_id = ?");
        ps.setLong(1, id);
        rs = ps.executeQuery();

        while (rs.next()) {
            mesh.getComments().add(rs.getLong("id"));
        }

        rs.close();
        ps.close();
        connection.close();

        return mesh;
    }

//    public void update(String token, long id, String newName, String newDescription) {}
//    public void delete(String token, long id) {}
//    public List<Mesh> search(String query) {}
//    public void like(String token, long meshId, long userId) {}
//    public void unlike(String token, long meshId, long userId) {}
//    public void comment(String token, long meshId, String message) {}
}
