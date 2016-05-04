package controllers;

import models.User;
import models.UserShort;
import services.UserService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

/**
 * Created by Alexander on 20/03/16.
 */

@Path("/users")
public class UserController {
    @Inject
    private UserService userService;

    @POST
    @Produces("application/json")
    public Response create(@FormParam("login") String login,
                         @FormParam("password") String password,
                         @FormParam("name") String name) {
        try {
            userService.create(login, password, name);
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }

        return Response.status(200).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response read(@PathParam("id") long id) {
        UserShort user;
        try {
            user = userService.readShort(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        return Response.status(200).entity(user).build();
    }
}
