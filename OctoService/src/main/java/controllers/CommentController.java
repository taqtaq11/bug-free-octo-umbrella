package controllers;

import models.Comment;
import models.User;
import services.CommentService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by Alexander on 07/05/16.
 */

@Path("/comments")
public class CommentController {

    @Inject
    CommentService commentService;

    // Read
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response read(@PathParam("id") long id) {
        try {
            Comment comment = commentService.read(id);
            return Response.status(200).entity(comment).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }
}
