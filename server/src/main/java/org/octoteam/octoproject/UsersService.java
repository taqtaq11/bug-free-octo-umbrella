package org.octoteam.octoproject;

import org.octoteam.octoproject.Entities.User;

import javax.json.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Alexander on 18/02/16.
 */

@Path("/users")
public class UsersService {

    @POST
    @Path("/get")
    @Produces("application/json")
    public Response get(@DefaultValue("-1") @FormParam("user_id") long user_id) {

        if (user_id == -1) {
            JsonObject resp = Json.createObjectBuilder().add("error", "User id must be specified.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();

        User user;
        try {
            user = em.find(User.class, user_id);
        } catch (Exception e) {
            em.close();
            emf.close();

            JsonObject resp = Json.createObjectBuilder().add("error", "Wrong user id.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        String resp = user.toFullJSON().toString();

        em.close();
        emf.close();

        return Response.status(200).entity(resp).build();
    }

    // List todo search
    @POST
    @Path("/search")
    @Produces("application/json")
    public Response search() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT u FROM User u");
        List<User> users = query.getResultList();

        JsonArrayBuilder jsonUsersBuilder = Json.createArrayBuilder();

        for (User user : users) {
            jsonUsersBuilder.add(user.toShortJSON());
        }

        em.close();
        emf.close();

        return Response.status(200).entity(jsonUsersBuilder.build().toString()).build();
    }

    @POST
    @Path("/update")
    @Produces("application/json")
    public Response update(@DefaultValue("") @FormParam("token") String token,
                           @DefaultValue("") @FormParam("name") String name,
                           @DefaultValue("") @FormParam("old_password") String old_password,
                           @DefaultValue("") @FormParam("new_password") String new_password,
                           @DefaultValue("") @FormParam("avatar_file") String avatar_file,
                           @Context HttpServletRequest request) {

        long id = SessionManager.checkSession(token, request.getRemoteAddr());

        if (id == -1) {
            JsonObject resp = Json.createObjectBuilder().add("error", "Wrong token.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        User user = em.find(User.class, id);

        JsonObjectBuilder resp = Json.createObjectBuilder();

        if (!name.isEmpty()) {
            user.setName(name);
            resp.add("name_change", "ok");
        } else {
            resp.add("name_change", "The new name is not specified.");
        }

        if (!new_password.isEmpty()) {
            if (user.getPassword().equals(HashUtils.md5(old_password))) {
                user.setPassword(HashUtils.md5(new_password));
                resp.add("password_change", "ok");
            } else {
                resp.add("password_change", "Old password in not correct.");
            }
        } else {
            resp.add("password_change", "The new password is not specified.");
        }

        if (!avatar_file.isEmpty()) {
            user.setAvatar_file(avatar_file);
            resp.add("avatar_change", "ok");
        } else {
            resp.add("avatar_change", "The new avatar is not specified.");
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        return Response.status(200).entity(resp.build().toString()).build();
    }

    @POST
    @Path("/follow")
    @Produces("application/json")
    public Response follow(@DefaultValue("") @FormParam("token") String token,
                             @DefaultValue("-1") @FormParam("user_id") long user_id,
                             @Context HttpServletRequest request) {
        long id = SessionManager.checkSession(token, request.getRemoteAddr());

        if (id == -1) {
            JsonObject resp = Json.createObjectBuilder().add("error", "Wrong token.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        User user = em.find(User.class, id);

        User toFollow;
        try {
            toFollow = em.find(User.class, user_id);
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            emf.close();

            JsonObject resp = Json.createObjectBuilder().add("error", "Can not find user to follow.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        JsonObjectBuilder resp = Json.createObjectBuilder();

        if (!user.getSubscriptions().contains(toFollow)) {
            user.getSubscriptions().add(toFollow);

            resp.add("status", "ok");
        } else {
            resp.add("error", "You are already subscribed to user.");
        }

        em.getTransaction().commit();
        em.close();
        emf.close();

        return Response.status(200).entity(resp.build().toString()).build();
    }

    @POST
    @Path("/unfollow")
    @Produces("application/json")
    public Response unfollow(@DefaultValue("") @FormParam("token") String token,
                             @DefaultValue("-1") @FormParam("user_id") long user_id,
                             @Context HttpServletRequest request) {
        long id = SessionManager.checkSession(token, request.getRemoteAddr());

        if (id == -1) {
            JsonObject resp = Json.createObjectBuilder().add("error", "Wrong token.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        User user = em.find(User.class, id);

        User toUnfollow;
        try {
            toUnfollow = em.find(User.class, user_id);
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            emf.close();

            JsonObject resp = Json.createObjectBuilder().add("error", "Can not find user to unfollow.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        JsonObjectBuilder resp = Json.createObjectBuilder();

        if (user.getSubscriptions().contains(toUnfollow)) {
            user.getSubscriptions().remove(toUnfollow);

            resp.add("status", "ok");
        } else {
            resp.add("error", "You are not subscribed to user.");
        }

        em.getTransaction().commit();
        em.close();
        emf.close();

        return Response.status(200).entity(resp.build().toString()).build();
    }
}
