package com.givaudan.galaxy.dao;

import java.io.Serializable;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateful
public class EntityManagerDao implements Serializable {
    
    @PersistenceContext(unitName="galaxy_PU", type=PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
 
    public Object updateObject(Object object) {
        return entityManager.merge(object);
    }
 
    public void createObject(Object object) {
        entityManager.persist(object);
    }
 
    public void refresh(Object object) {
        entityManager.refresh(object);
    }
 
    public <T> T find(Class<T> clazz, Long id) {
        return entityManager.find(clazz, id);
    }
 
    public void deleteObject(Object object) {
        entityManager.remove(entityManager.contains(object) ? object : entityManager.merge(object));
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
