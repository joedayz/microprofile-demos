
package com.example.guestbook.util;

import com.example.guestbook.model.Message;
import org.apache.commons.lang3.StringUtils;

import javax.json.*;
import java.util.List;

import static javax.json.Json.createObjectBuilder;

public class MessageUtil {

    public static JsonObject buildMessageJson(List<Message> messages, String url, Integer pageSize) {
        return createObjectBuilder()
                .add("_embedded", MessageUtil.buildMessages(messages, url))
                .add("_links", MessageUtil.buildLinks(url))
                .add("page", MessageUtil.buildPaginationInfo(messages, pageSize))
                .build();
    }

    public static JsonObject buildMessageById(Message message, String url) {
        JsonObjectBuilder objectBuilder = createObjectBuilder();
        objectBuilder
                .add("username", message.getUsername())
                .add("message", message.getMessage())
                .add("timestamp", message.getTimestamp().toString())
                .add("_links", MessageUtil.buildLinksForMessageById(url));

        return objectBuilder.build();
    }

    private static JsonObject buildLinksForMessageById(String invUri) {
        JsonObjectBuilder links = createObjectBuilder();
        links.add("self", Json.createObjectBuilder()
                .add("href", StringUtils.appendIfMissing(invUri, "") ));


        links.add("message", Json.createObjectBuilder()
                .add("href", StringUtils.appendIfMissing(invUri, "") ));

        return links.build();
    }

    private static JsonObject buildPaginationInfo(List<Message> messages, Integer pageSize) {

        JsonObjectBuilder links = createObjectBuilder();

        links.add("size", pageSize);
        links.add("totalElements", messages.size());
        int cociente = messages.size()/pageSize;
        int residuo = messages.size()%pageSize;
        links.add("totalPages", residuo==0?cociente:cociente+1);
        links.add("number", 0);

        return links.build();
    }

    private static JsonObject buildLinks(String invUri) {
        JsonObjectBuilder links = createObjectBuilder();

        links.add("self", Json.createObjectBuilder()
                .add("href", StringUtils.appendIfMissing(invUri, "{?page,size,sort}"))
                .add("templated", "true"));


        links.add("profile", Json.createObjectBuilder()
                .add("href", StringUtils.appendIfMissing(invUri.replace("messages", "profile"), "/messages")));

        return links.build();

    }

    private static JsonObject buildMessages(List<Message> messages, String url) {

        JsonObjectBuilder objectBuilder = createObjectBuilder();
        objectBuilder.add("messages", MessageUtil.buildMessagesFromDatabase(messages, url));

        return objectBuilder.build();
    }

    private static JsonArray buildMessagesFromDatabase(List<Message> messages, String url) {

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for(Message message: messages){
            jsonArrayBuilder.add(createObjectBuilder()
                    .add("username", message.getUsername())
                    .add("message", message.getMessage())
                    .add("timestamp", message.getTimestamp().toString())
                    .add("_links", MessageUtil.buildLinksForMessage(message.getId().toString(), url)));
        }



        return jsonArrayBuilder.build();

    }

    private static JsonObject buildLinksForMessage(String messageId, String invUri) {

        JsonObjectBuilder links = createObjectBuilder();
        links.add("self", Json.createObjectBuilder()
                .add("href", StringUtils.appendIfMissing(invUri, "/") + messageId));


        links.add("message", Json.createObjectBuilder()
                .add("href", StringUtils.appendIfMissing(invUri, "/") + messageId));

        return links.build();
    }



}
 