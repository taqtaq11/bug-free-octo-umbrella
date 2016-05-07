package services;

import models.Mesh;
import models.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.resource.spi.ConnectionDefinition;
import java.sql.*;
import java.util.LinkedList;
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
        ps = connection.prepareStatement("SELECT * FROM public.comment WHERE mesh_id = ? ORDER BY timestamp");
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

    // TODO: 07/05/16 realize
//    public void update(String token, long id, String newName, String newDescription) {}

    public void delete(String token, String ip, long id) throws Exception {
        long userId = sessionService.check(token, ip);
        Mesh mesh = read(id);

        if (mesh.getAuthor() != userId) {
            throw new Exception("You can not delete not your mesh.");
        }

        Connection connection = connectionService.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM public.mesh WHERE id = ?");
        ps.setLong(1, id);
        ps.execute();
        ps.close();
        connection.close();
    }

    public List<Mesh> search(String query) throws Exception {
        List<Mesh> meshes = new LinkedList<>();

        Connection connection = connectionService.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM public.mesh");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            meshes.add(read(rs.getLong("id")));
        }

        rs.close();
        ps.close();
        connection.close();

        return meshes;
    }

    public void like(String token, String ip, long meshId, long passedUserId) throws Exception {
        long userId = sessionService.check(token, ip);

        if (userId != passedUserId) {
            throw new Exception("You can like only using your id.");
        }

        Connection connection = connectionService.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO public.like(mesh_id, user_id) VALUES (?, ?)");
        ps.setLong(1, meshId);
        ps.setLong(2, userId);
        ps.execute();

        ps.close();
        connection.close();
    }

    public void unlike(String token, String ip, long meshId, long passedUserId) throws Exception {
        long userId = sessionService.check(token, ip);

        if (userId != passedUserId) {
            throw new Exception("You can unlike only using your id.");
        }

        Connection connection = connectionService.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM public.like WHERE mesh_id = ? and user_id = ?");
        ps.setLong(1, meshId);
        ps.setLong(2, userId);
        ps.execute();

        ps.close();
        connection.close();
    }

    public void comment(String token, String ip, long meshId, String message) throws Exception {
        long userId = sessionService.check(token, ip);

        Connection connection = connectionService.getConnection();
        PreparedStatement ps =connection
                .prepareStatement("INSERT INTO public.comment(user_id, mesh_id, message, timestamp) VALUES (?, ?, ?, ?)");
        ps.setLong(1, userId);
        ps.setLong(2, meshId);
        ps.setString(3, message);
        ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        ps.execute();

        ps.close();
        connection.close();
    }
}
