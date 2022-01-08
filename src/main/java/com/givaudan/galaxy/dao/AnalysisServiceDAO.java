package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.model.AnalysisCheckEnd;
import com.givaudan.galaxy.model.AnalysisReferenceData;
import com.givaudan.galaxy.model.MethodGC;
import com.givaudan.galaxy.model.ProgramGC;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.analysis.GC;
import com.givaudan.galaxy.service.AnalysisCheckEndObjectService;
import com.givaudan.galaxy.service.AnalysisObjectService;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

@Stateless
public class AnalysisServiceDAO {

    private static final Logger logger = Logger.getLogger(AnalysisServiceDAO.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public Analysis findAnalysisById(Long id) {
//        logger.info("findAnalysisById called");

        try {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Analysis> query = cb.createQuery(Analysis.class);
            Root<Analysis> object = query.from(Analysis.class);
            query.where(cb.equal(object.get("id"), id));
            Analysis analysis = entityManager.createQuery(query).getSingleResult();

            //fetch
            analysis.getStandards().size();

            return analysis;

        } catch (NoResultException | IllegalArgumentException nre) {
//            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            return null;
        }
    }

    public Analysis findAnalysisByNumber(String number) {
//        logger.info("findAnalysisByNumber called");

        try {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Analysis> query = cb.createQuery(Analysis.class);
            Root<Analysis> object = query.from(Analysis.class);
            query.where(cb.equal(object.get("number"), number));
            Analysis analysis = entityManager.createQuery(query).getSingleResult();

            //fetch
            analysis.getStandards().size();

            return analysis;

        } catch (NoResultException | IllegalArgumentException nre) {
//            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            return null;
        }
    }

    public GC findGCByNumber(String number) {
//        logger.info("findGCByNumber called");

        try {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<GC> query = cb.createQuery(GC.class);
            Root<GC> object = query.from(GC.class);
            Fetch<GC, ProgramGC> fetch1 = object.fetch("program", JoinType.LEFT);
            Fetch<GC, MethodGC> fetch2 = object.fetch("method", JoinType.LEFT);
            query.where(cb.equal(object.get("number"), number));
            GC analysis = entityManager.createQuery(query).getSingleResult();

            //fetch
            analysis.getStandards().size();
            analysis.getProgram().getColumn().getId();
            analysis.getProduct().getName_en();

            return analysis;

        } catch (NoResultException | IllegalArgumentException nre) {
//            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            return null;
        }
    }

    public AnalysisReferenceData findReferenceByNumber(String number) {
//        logger.info("findReferenceByNumber called");

        try {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<AnalysisReferenceData> query = cb.createQuery(AnalysisReferenceData.class);
            Root<AnalysisReferenceData> object = query.from(AnalysisReferenceData.class);
            query.where(cb.equal(object.get("number"), number));
            AnalysisReferenceData result = entityManager.createQuery(query).getSingleResult();

            return result;

        } catch (NoResultException | IllegalArgumentException nre) {
//            logger.log(Level.SEVERE, "Error findByNumber : {0}", nre.getMessage());
            return null;
        }
    }

    public AnalysisCheckEndObjectService findAnalysisCheckEndByProductCodeAndOperation(String productCode, String operation) {
//        logger.info("findAnalysisCheckEndByProductCodeAndOperation called");

        //les parameters ne peuvent pas être nuls
        if(productCode == null || operation == null) return null;
        
        try {

            AnalysisCheckEnd analysisCheckEnd = (AnalysisCheckEnd) entityManager.createNamedQuery("AnalysisCheckEnd.findAnalysisCheckEndWithProductCodeAndOperation")
                    .setParameter("code", productCode)
                    .setParameter("operation", operation)
                    .getSingleResult();

            AnalysisCheckEndObjectService result = new AnalysisCheckEndObjectService();
            result.setAnalysisCheckEndEnum(analysisCheckEnd.getAnalysisCheckEndEnum());
            result.setCheckAppearance(analysisCheckEnd.isCheckAppearance());
            result.setCheckCO(analysisCheckEnd.isCheckCO());
            result.setCheckSAP(analysisCheckEnd.isCheckSAP());
            result.setInformation(analysisCheckEnd.getInformation());
            result.setInformationAnalysis(analysisCheckEnd.getInformationAnalysis());
            result.setOperation(analysisCheckEnd.getOperation());
            result.setProductCodeStart(analysisCheckEnd.getProductCodeStart());
            result.setProductNameStart(analysisCheckEnd.getProductNameStart());

            if(analysisCheckEnd.isAnalysisListWired()) {
                analysisCheckEnd.getAnalysisList().stream().map((item) -> {
                    item.getStandards().size();
                    return item;
                }).forEach((item) -> {
                    result.getAnalysisList().add(item);
                });
            }

            return result;
            
        } catch (NoResultException | IllegalArgumentException nre) {
//            logger.log(Level.SEVERE, "Error findByNumber : {0}", nre.getMessage());
            return null;
        }
    }
}
