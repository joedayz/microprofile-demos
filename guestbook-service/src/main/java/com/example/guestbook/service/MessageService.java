// tag::comment[]
/*******************************************************************************
 * Copyright (c) 2017, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::comment[]
package com.example.guestbook.service;

import com.example.guestbook.model.Message;
import com.example.guestbook.repository.MessageRepository;
import com.example.guestbook.util.MessageUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Stateless
public class MessageService {

    private ConcurrentMap<String, JsonObject> inv = new ConcurrentHashMap<>();

    @Inject
    private MessageRepository messageRepository;

    @Inject
    @ConfigProperty(name = "page.size")
    private Integer pageSize;

    public JsonObject getMessages(String url) {
        List<Message> messages = messageRepository.listAll(0, pageSize);
        JsonObject content = MessageUtil.buildMessageJson(messages, url, pageSize);
        return content;
    }

    public JsonObject getMessage(String id, String url) {
        Message message = messageRepository.findById(Long.valueOf(id));
        JsonObject content = MessageUtil.buildMessageById(message, url);
        return content;
    }

    @Transactional
    public void add(Message message) {
        messageRepository.create(message);
    }


}
 