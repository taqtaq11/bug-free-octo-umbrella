package controllers;

import models.Session;
import services.SessionService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

/**
 * Created by Alexander on 21/03/16.
 */

@Path("/auth")
public class SessionController {
    @Inject
    SessionService sessionService;

    @POST
    @Produces("application/json")
    @Path("/signin")
    public Response signin(@FormParam("login") String login,
                             @FormParam("password") String password,
                             @Context HttpServletRequest request) {
        try {
            Session data = sessionService.create(login, password, request.getRemoteAddr());
            return Response.status(200).entity(data).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // TODO: 05/05/16 test method, delete later
    @POST
    @Produces("application/json")
    @Path("/check")
    public Response check(@FormParam("token") String token,
                          @Context HttpServletRequest request) {
        try {
            long userId = sessionService.check(token, request.getRemoteAddr());
            JsonObject res = Json.createObjectBuilder().add("user_id", userId).build();
            return Response.status(200).entity(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    @POST
    @Produces("application/json")
    @Path("/signout")
    public Response signout(@FormParam("token") String token,
                          @Context HttpServletRequest request) {
        try {
            sessionService.close(token, request.getRemoteAddr());
            return Response.status(200).build();
        } catch (SQLException e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }
}
