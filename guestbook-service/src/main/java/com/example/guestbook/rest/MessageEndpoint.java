package com.example.guestbook.rest;

import com.example.guestbook.model.Message;
import com.example.guestbook.service.MessageService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 */
@RequestScoped
 @Path("messages")
public class MessageEndpoint {

    @Inject
    MessageService manager;

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject handler() {
        return manager.getMessages(uriInfo.getAbsolutePath().toString());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject handlerId(@PathParam("id") String id) {
         return manager.getMessage(id, uriInfo.getAbsolutePath().toString());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Message message) {
        manager.add(message);
        return Response.ok().build();
    }

}
