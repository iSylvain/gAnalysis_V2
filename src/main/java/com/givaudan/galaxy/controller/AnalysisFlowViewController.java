package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.EntityManagerDao;
import com.givaudan.galaxy.exception.NoEntityException;
import com.givaudan.galaxy.model.AnalysisFlow;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Named
@ViewScoped
public class AnalysisFlowViewController implements Serializable {

    private static final Logger logger = Logger.getLogger(AnalysisFlowViewController.class.getName());

    @Inject
    private EntityManagerDao entityManagerDAO;

    private Long id;
    private AnalysisFlow instance;

    public void load() {
        logger.info("load called");

        try {
            CriteriaBuilder cb = entityManagerDAO.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<AnalysisFlow> query = cb.createQuery(AnalysisFlow.class);
            Root<AnalysisFlow> object = query.from(AnalysisFlow.class);
            query.where(cb.equal(object.get("id"), id));
            instance = entityManagerDAO.getEntityManager().createQuery(query).getSingleResult();
        } catch (NoResultException | NonUniqueResultException nre) {
            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            throw new NoEntityException("No entity with id : " + id);
        }
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the instance
     */
    public AnalysisFlow getInstance() {
        return instance;
    }

}
