package controllers;

import models.User;
import services.UserService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Alexander on 20/03/16.
 */

@Path("/users")
public class UserController {
    @Inject
    private UserService userService;

    // Create
    @POST
    @Produces("application/json")
    public Response create(@FormParam("login") String login,
                         @FormParam("password") String password,
                         @FormParam("name") String name) {
        try {
            userService.create(login, password, name);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }

    }

    // Read
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response read(@PathParam("id") long id) {
        try {
            User user = userService.read(id);
            return Response.status(200).entity(user.toJsonObject()).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // TODO: 06/05/16 realize
    // Update

    // Search
    @GET
    @Produces("application/json")
    public Response search(@QueryParam("query") String query) {
        try {
            List<User> searchResult = userService.search(query);

            JsonArrayBuilder resultJson = Json.createArrayBuilder();
            searchResult.forEach(x -> resultJson.add(x.toJsonObject()));

            return Response.status(200).entity(resultJson.build()).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // Follow
    @POST
    @Path("/{id}/subscriptions")
    @Produces("application/json")
    public Response follow(@PathParam("id") long id,
                           @FormParam("to_follow_id") long toFollowId,
                           @FormParam("token") String token,
                           @Context HttpServletRequest request) {
        try {
            userService.follow(token, request.getRemoteAddr(), id, toFollowId);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // Unfollow
    @DELETE
    @Path("/{id}/subscriptions/{to_unfollow_id}")
    @Produces("application/json")
    public Response unfollow(@PathParam("id") long id,
                           @PathParam("to_unfollow_id") long toUnfollowId,
                           @QueryParam("token") String token,
                           @Context HttpServletRequest request) {
        try {
            userService.unfollow(token, request.getRemoteAddr(), id, toUnfollowId);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }
}
