package controllers;

import models.Mesh;
import services.MeshService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by Alexander on 06/05/16.
 */

@Path("/meshes")
public class MeshController {

    @Inject
    MeshService meshService;

    // Create
    @POST
    @Produces("application/json")
    public Response create(@FormParam("token") String token,
                           @Context HttpServletRequest request,
                           @FormParam("name") String name,
                           @FormParam("description") String description,
                           @FormParam("obj_file") String objFile,
                           @FormParam("texture_file") String textureFile) {
        try {
            meshService.create(token, request.getRemoteAddr(), name, description, objFile, textureFile);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // Read
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response create(@PathParam("id") long id) {
        try {
            Mesh mesh = meshService.read(id);
            return Response.status(200).entity(mesh).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // Update
    // Delete
    // Search
    // Like
    // Unlike
    // Comment
}
