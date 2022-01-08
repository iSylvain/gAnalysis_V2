package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.exception.NoEntityException;
import com.givaudan.galaxy.model.AnalysisData;
import com.givaudan.galaxy.model.AnalysisReferenceData;
import com.givaudan.galaxy.model.MethodGC;
import com.givaudan.galaxy.model.ProgramGC;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.analysis.GC;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

@Stateless
public class AnalysisDAO {
    
    private static final Logger logger = Logger.getLogger(AnalysisDAO.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public Analysis findAnalysisById(Long id) {

        try {
            logger.info("findAnalysisById called");
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Analysis> query = cb.createQuery(Analysis.class);
            Root<Analysis> object = query.from(Analysis.class);
            Fetch<Analysis, AnalysisData> fetch1 = object.fetch("referenceData", JoinType.LEFT);
            query.where(cb.equal(object.get("id"), id));
            Analysis analysis = entityManager.createQuery(query).getSingleResult();

            //fetch
            analysis.getStandards().size();
            analysis.getDatas().size();

            return analysis;

        } catch (NoResultException | NonUniqueResultException nre) {
            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            throw new NoEntityException("No entity with id : " + id);
        }
    }

    public GC findAnalysisGCById(Long id) {

        try {
            logger.info("findAnalysisGCById called");
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<GC> query = cb.createQuery(GC.class);
            Root<GC> object = query.from(GC.class);
            Fetch<GC, ProgramGC> fetch1 = object.fetch("program", JoinType.LEFT);
            Fetch<GC, MethodGC> fetch2 = object.fetch("method", JoinType.LEFT);
            Fetch<GC, AnalysisData> fetch3 = object.fetch("referenceData", JoinType.LEFT);
            query.where(cb.equal(object.get("id"), id));
            GC analysis = entityManager.createQuery(query).getSingleResult();

            //fetch
            analysis.getStandards().size();
            analysis.getDatas().size();

            return analysis;

        } catch (NoResultException | NonUniqueResultException nre) {
            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            throw new NoEntityException("No entity with id : " + id);
        }
    }

    public AnalysisReferenceData findAnalysisReferenceDataByAnalysisID(Long id) {

        try {
            logger.info("findAnalysisReferenceDataByAnalysisID called");
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Analysis> query = cb.createQuery(Analysis.class);
            Root<Analysis> object = query.from(Analysis.class);
            Fetch<Analysis, AnalysisData> fetch = object.fetch("referenceData", JoinType.LEFT);
            query.where(cb.equal(object.get("id"), id));
            Analysis analysis = entityManager.createQuery(query).getSingleResult();
            
            return analysis.getReferenceData();

        } catch (NoResultException | NonUniqueResultException nre) {
            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            throw new NoEntityException("No reference with id : " + id);
        }
    }

    public AnalysisReferenceData findAnalysisReferneceDataById(Long idData) {
        return entityManager.find(AnalysisReferenceData.class, idData);
    }

    public AnalysisData findAnalysisDataById(Long idData) {
        return entityManager.find(AnalysisData.class, idData);
    }

}
