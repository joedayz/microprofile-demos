package com.example.guestbook.model;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="message")
@NamedQueries({
        @NamedQuery(name = Message.QUERY_FIND_ALL, query = "SELECT m FROM Message m")
})
public class Message implements Serializable {


    public static final String QUERY_FIND_ALL = "Message.findAll";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String message;


    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}