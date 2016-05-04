package controllers;

import models.SessionData;
import services.SessionService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

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

        SessionData data;
        try {
            data = sessionService.create(login, password, request.getRemoteAddr());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        return Response.status(200).entity(data).build();
    }
}
