package org.octoteam.octoproject;

import org.octoteam.octoproject.Entities.User;

import javax.json.Json;
import javax.json.JsonObject;
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
 * Created by Alexander on 23/02/16.
 */

@Path("/auth")
public class AuthService {
    @POST
    @Path("/signup")
    @Produces("application/json")
    public Response signup(@DefaultValue("") @FormParam("name") String name,
                           @DefaultValue("") @FormParam("login") String login,
                           @DefaultValue("") @FormParam("password") String password,
                           @Context HttpServletRequest request) {

        if (name.equals("")) {
            JsonObject resp = Json.createObjectBuilder().add("error", "Name must be specified.").build();
            return Response.status(200).entity(resp.toString()).build();
        }
        if (login.equals("")) {
            JsonObject resp = Json.createObjectBuilder().add("error", "Login must be specified.").build();
            return Response.status(200).entity(resp.toString()).build();
        }
        if (password.equals("")) {
            JsonObject resp = Json.createObjectBuilder().add("error", "Password must be specified.").build();
            return Response.status(200).entity(resp.toString()).build();
        }
        
        // // TODO: 28/02/16 validation

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT u FROM User u where u.login = :login");
        query.setParameter("login", login);

        if (query.getResultList().size() != 0) {
            em.close();
            emf.close();

            JsonObject resp = Json.createObjectBuilder().add("error", "Login is already in use.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        User user;
        try {
            em.getTransaction().begin();
            user = new User(name, login, HashUtils.md5(password));
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            emf.close();

            JsonObject resp = Json.createObjectBuilder().add("error", "Insertion error.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        em.close();
        emf.close();

        String token = SessionManager.createSession(user.getId(), request.getRemoteAddr());

        JsonObject resp = Json.createObjectBuilder().add("user_id", user.getId()).add("token", token).build();
        return Response.status(200).entity(resp.toString()).build();
    }

    @POST
    @Path("/signin")
    @Produces("application/json")
    public Response signin(@DefaultValue("") @FormParam("login") String login,
                           @DefaultValue("") @FormParam("password") String password,
                           @Context HttpServletRequest request) {

        if (login.equals("")) {
            JsonObject resp = Json.createObjectBuilder().add("error", "Login must be specified.").build();
            return Response.status(200).entity(resp.toString()).build();
        }
        if (password.equals("")) {
            JsonObject resp = Json.createObjectBuilder().add("error", "Password must be specified.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT u FROM User u where u.login = :login and u.password = :password");
        query.setParameter("login", login);
        query.setParameter("password", HashUtils.md5(password));
        List<User> users = query.getResultList();

        if (users.size() == 0) {
            em.close();
            emf.close();

            JsonObject resp = Json.createObjectBuilder().add("error", "Invalid login data.").build();
            return Response.status(200).entity(resp.toString()).build();
        }

        User user = users.get(0);

        em.close();
        emf.close();

        String token = SessionManager.createSession(user.getId(), request.getRemoteAddr());

        JsonObject resp = Json.createObjectBuilder().add("user_id", user.getId()).add("token", token).build();
        return Response.status(200).entity(resp.toString()).build();
    }

    @POST
    @Path("/signout")
    @Produces("application/json")
    public Response signout(@FormParam("token") String token, @Context HttpServletRequest request) {
        SessionManager.closeSession(token, request.getRemoteAddr());

        JsonObject resp = Json.createObjectBuilder().add("status", "Session closed.").build();
        return Response.status(200).entity(resp.toString()).build();
    }
}
