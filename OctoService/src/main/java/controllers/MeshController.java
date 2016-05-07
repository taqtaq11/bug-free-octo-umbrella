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
import java.util.List;

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

    // TODO: 07/05/16 realize
    // Update

    // Delete
    @DELETE
    @Produces("application/json")
    @Path("/{id}")
    public Response delete(@QueryParam("token") String token,
                           @Context HttpServletRequest request,
                           @PathParam("id") long id) {
        try {
            meshService.delete(token, request.getRemoteAddr(), id);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // Search
    @GET
    @Produces("application/json")
    public Response delete(@QueryParam("query") String query) {
        try {
            List<Mesh> result = meshService.search(query);
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // Like
    @POST
    @Produces("application/json")
    @Path("{mesh_id}/likes")
    public Response like(@FormParam("token") String token,
                         @Context HttpServletRequest request,
                         @PathParam("mesh_id") long meshId,
                         @FormParam("user_id") long passedUserId) {
        try {
            meshService.like(token, request.getRemoteAddr(), meshId, passedUserId);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // Unlike
    @DELETE
    @Produces("application/json")
    @Path("{mesh_id}/likes/{user_id}")
    public Response unlike(@QueryParam("token") String token,
                           @Context HttpServletRequest request,
                           @PathParam("mesh_id") long meshId,
                           @PathParam("user_id") long passedUserId) {
        try {
            meshService.unlike(token, request.getRemoteAddr(), meshId, passedUserId);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }

    // Comment
    @POST
    @Produces("application/json")
    @Path("{mesh_id}/comments")
    public Response comment(@FormParam("token") String token,
                            @Context HttpServletRequest request,
                            @PathParam("mesh_id") long meshId,
                            @FormParam("message") String message
                            ) {
        try {
            meshService.comment(token, request.getRemoteAddr(), meshId, message);
            return Response.status(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
            return Response.status(400).entity(err).build();
        }
    }
}
