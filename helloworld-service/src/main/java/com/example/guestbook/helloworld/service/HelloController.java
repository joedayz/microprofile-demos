package com.example.guestbook.helloworld.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Path("/hello")
@Singleton
public class HelloController {


    @Inject
    @ConfigProperty(name="greeting")
    private  String greetingTemplate;

    private final String version = "1.0";

    @GET
    @Path("{name}")
    @Traced(operationName = "sayHello")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> sayHello(@PathParam("name") String name) 
    throws UnknownHostException {

        Map<String, String> response = new HashMap<>();

        String hostname = InetAddress.getLocalHost().getHostName();
        String greeting = greetingTemplate
                .replaceAll("\\$name", name)
                .replaceAll("\\$hostname", hostname)
                .replaceAll("\\$version", version);

        response.put("greeting", greeting);
        response.put("version", version);
        response.put("hostname", hostname);

        return   response;
    }
}