package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.model.core.Notification;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class NotificationDAO {
    
    private static final Logger logger = Logger.getLogger(NotificationDAO.class.getName());

    private final String module = "gAnalysis";
    
    @PersistenceContext
    private EntityManager entityManager;

    public Notification findById(Long id) {

        try {
            logger.info("findById called");
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Notification> query = cb.createQuery(Notification.class);
            Root<Notification> object = query.from(Notification.class);
            query.where(cb.equal(object.get("id"), id));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException | NonUniqueResultException nre) {
            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            return null;
        }
    }

    public List<Notification> findAllWithModule() {

        try {
            logger.info("findAllWithModule called");
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Notification> query = cb.createQuery(Notification.class);
            Root<Notification> object = query.from(Notification.class);
            query.where(cb.equal(object.get("module"), module));
            query.select(object);
            return entityManager.createQuery(query).getResultList();
        } catch (NoResultException | IllegalArgumentException nre) {
            logger.log(Level.INFO, "Error findAll : {0}", nre.getMessage());
            return null;
        }
    }

    public List<Notification> findAllWithModuleAndLimit(Integer number) {

        try {
            logger.info("findAllWithModuleAndLimit called");
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Notification> query = cb.createQuery(Notification.class);
            Root<Notification> object = query.from(Notification.class);
            query.where(cb.equal(object.get("module"), module));
            query.select(object);
            
            query.orderBy(cb.desc(object.get("createdOn")));
            TypedQuery<Notification> tq = entityManager.createQuery(query);
            tq.setMaxResults(number);
            return tq.getResultList();
        } catch (NoResultException | IllegalArgumentException nre) {
            logger.log(Level.INFO, "Error findAllWithLimit : {0}", nre.getMessage());
            return null;
        }
    }

}
