package com.example.guestbook.repository;


import com.example.guestbook.model.Message;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MessageRepository {


    @PersistenceContext(name = "GuestBookDS")
    private EntityManager em;

    public void create(Message entity) {
        em.persist(entity);
    }

    public void deleteById(Long id) {
        Message entity = em.find(Message.class, id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public Message findById(Long id) {
        return em.find(Message.class, id);
    }

    public Message update(Message entity) {
        return em.merge(entity);
    }

    public List<Message> listAll(Integer startPosition, Integer maxResult) {
        TypedQuery<Message> findAllQuery = em.createQuery(
                "SELECT DISTINCT m FROM Message m ORDER BY m.id",
                Message.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        return findAllQuery.getResultList();
    }
}
