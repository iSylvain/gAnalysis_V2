package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.exception.NoEntityException;
import com.givaudan.galaxy.model.AnalysisCheckEnd;
import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.AnalysisForFlow;
import com.givaudan.galaxy.model.AnalysisStandard;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.analysis.Density;
import com.givaudan.galaxy.model.analysis.Divers;
import com.givaudan.galaxy.model.analysis.IR;
import com.givaudan.galaxy.model.analysis.KF;
import com.givaudan.galaxy.model.analysis.PH;
import com.givaudan.galaxy.model.analysis.Refraction;
import com.givaudan.galaxy.model.analysis.Titration;
import com.givaudan.galaxy.model.core.Notification;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;

public class AnalysisEditDAO implements Serializable {

    private static final Logger logger = Logger.getLogger(AnalysisEditDAO.class.getName());

    @Inject
    private EntityManagerDao entityManagerDAO;

    @Inject
    private Identity identity;

    @Inject
    private Conversation conversation;

    private Long id;
    private Long idAnalysis;

    private Analysis instance;
    private Product product;
    private AnalysisContainer container;
    private AnalysisStandard analysisStandard;

    @PreDestroy
    public void cleanObject() {
        instance = null;
        product = null;
        container = null;
        analysisStandard = null;
    }
    
    private String analysisType;

    private boolean analysisDeletable = true;

    public boolean isManaged() {
        return getInstance().getId() != null;
    }

    public void initConversation() {
        logger.info("initConversation called.");

        if (conversation.isTransient()) {

            try {
                product = (Product) entityManagerDAO.getEntityManager().createNamedQuery("Product.findById").setParameter("id", getId()).getSingleResult();
            } catch (NoResultException e) {
                throw new NoEntityException("No entity Product with id : " + getId());
            }

            try {
                container = (AnalysisContainer) entityManagerDAO.getEntityManager().createNamedQuery("AnalysisContainer.findByProductId").setParameter("id", getId()).getSingleResult();
            } catch (NoResultException e) {
                throw new NoEntityException("No entity Container with id : " + getId());
            }

            conversation.begin();
        }
    }

    public String save() {
        logger.info("save called.");

        if (isManaged()) {

            logger.info("update called.");
            try {

                entityManagerDAO.updateObject(instance);

                String message = new MessageProvider().getValue("module", "analysis_updated");
                message = message + ", type : " + instance.getTypeOfAnalysis() + " N°" + instance.getNumber();
                String messageInfo = product.getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "MOD");

            } catch (ConstraintViolationException cve) {

                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Analysis is not updated.", "");
                getConversation().end();
                return "error";
            }
        } else {

            logger.info("create called.");
            try {
                
                instance.setTypeOfAnalysis(analysisType);
                entityManagerDAO.createObject(instance);

                String message = new MessageProvider().getValue("module", "analysis_created");
                message = message + ", type : " + instance.getTypeOfAnalysis() + " N°" + instance.getNumber();
                String messageInfo = product.getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "ADD");

            } catch (ConstraintViolationException cve) {
                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Analysis is not created.", "");
                getConversation().end();
                return "error";
            }
        }

        getConversation().end();
        return "saved";
    }

    public String delete() {
        logger.info("delete called.");

        entityManagerDAO.deleteObject(instance);

        String message = new MessageProvider().getValue("module", "analysis_deleted");
        message = message + ", type : " + instance.getTypeOfAnalysis() + " N°" + instance.getNumber();
        String messageInfo = product.getName_en();
        MessageUtil.addSuccessMessage(message, messageInfo);

        conversation.end();

        createNotifciation(message, messageInfo, "DEL");

        return "deleted";
    }

    public String cancel() {
        logger.info("cancel called.");
        getConversation().end();
        return "cancelled";
    }

    private void createNotifciation(String message, String messageInfo, String type) {
        logger.info("createNotifciation called.");
        User user = (User) identity.getAccount();
        Notification notification = new Notification("gAnalysis",
                product.getId(),
                "Product",
                type,
                user.getLoginName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                message, messageInfo);
        entityManagerDAO.createObject(notification);
    }

    public Analysis getInstance() {

        if (instance == null) {
            try {
                CriteriaBuilder cb = entityManagerDAO.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<Analysis> query = cb.createQuery(Analysis.class);
                Root<Analysis> object = query.from(Analysis.class);
                query.where(cb.equal(object.get("id"), idAnalysis));
                instance = entityManagerDAO.getEntityManager().createQuery(query).getSingleResult();
                
                checkIfAnalysisDeletable();
                
            } catch (NoResultException nre) {
                logger.log(Level.INFO, "analysis type : {0}", analysisType);
                instance = initAnalysis(analysisType);
                instance.setAnalysisContainer(container);
                analysisDeletable = false;
            }
        }

        return instance;
    }

    private void checkIfAnalysisDeletable() {
        logger.info("checkIfAnalysisDeletable called.");

        //check si analyse utilisée dans flow
        List<AnalysisForFlow> analysisForFlowList = entityManagerDAO.getEntityManager().createNamedQuery("AnalysisForFlow.findAllAnalysisForFlowWithNumber").setParameter("number", instance.getNumber()).getResultList();
        if (analysisForFlowList != null && analysisForFlowList.size() > 0) {
            logger.info("flow wired");
            analysisDeletable = false;
        }

        //check si analyse utilisée dans check matière première
        if (container.isAnalysisCheckWired()) {
            List<Analysis> analysisList = entityManagerDAO.getEntityManager().createNamedQuery("AnalysisCheck.findAnalysisListWithId").setParameter("id", container.getId()).getResultList();
            if (analysisList != null && analysisList.size() > 0) {
                logger.log(Level.INFO, "analysisList.size(): {0}", analysisList.size());
                analysisList.stream().map((Analysis analysis) -> {
                    Long analysisId = analysis.getId();
                    return analysisId;
                }).forEach((analysisId) -> {
                    if (Objects.equals(analysisId, instance.getId())) {
                        logger.info("check wired");
                        analysisDeletable = false;
                    }
                });
            }
        }

        //check si analyse utilisée dans check brut
        if (container.isAnalysisCheckEndListWired()) {
            List<AnalysisCheckEnd> analysisCheckEndList = entityManagerDAO.getEntityManager().createNamedQuery("AnalysisCheckEnd.findAllAnalysisCheckEndWithProductId").setParameter("id", product.getId()).getResultList();
            if (analysisCheckEndList != null && analysisCheckEndList.size() > 0) {
                logger.log(Level.INFO, "analysisCheckEndList.size(): {0}", analysisCheckEndList.size());
                analysisCheckEndList.stream().filter((analysisCheckEnd) -> (analysisCheckEnd.isAnalysisListWired())).forEach((analysisCheckEnd) -> {
                    analysisCheckEnd.getAnalysisList().stream().filter((analysis) -> (Objects.equals(analysis.getId(), instance.getId()))).map((_item) -> {
                        logger.info("check end wired");
                        return _item;
                    }).forEach((_item) -> {
                        analysisDeletable = false;
                    });
                });
            }
        }

        logger.log(Level.INFO, "analysisDeletable: {0}", analysisDeletable);
    }

    private Analysis initAnalysis(String type) {

        if (type.equals("density")) {
            return new Density();
        }
        if (type.equals("refraction")) {
            return new Refraction();
        }
        if (type.equals("titration")) {
            return new Titration();
        }
        if (type.equals("kf")) {
            return new KF();
        }
        if (type.equals("ph")) {
            return new PH();
        }
        if (type.equals("ir")) {
            return new IR();
        }
        if (type.equals("divers")) {
            return new Divers();
        }

        return null;
    }

    public void addAnalysisStandard() {

        logger.log(Level.INFO, "addAnalysisStandard called");
        analysisStandard.setAnalysis(getInstance());
        getInstance().getStandards().add(analysisStandard);
        analysisStandard = new AnalysisStandard();
    }

    public void deleteAnalysisStandard(AnalysisStandard standard) {

        logger.log(Level.INFO, "deleteAnalysisStandard called");
        getInstance().getStandards().remove(standard);
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
     * @return the idAnalysis
     */
    public Long getIdAnalysis() {
        return idAnalysis;
    }

    /**
     * @param idAnalysis the idAnalysis to set
     */
    public void setIdAnalysis(Long idAnalysis) {
        this.idAnalysis = idAnalysis;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @return the container
     */
    public AnalysisContainer getContainer() {
        return container;
    }

    /**
     * @return the analysisStandard
     */
    public AnalysisStandard getAnalysisStandard() {
        return analysisStandard;
    }

    /**
     * @param analysisStandard the analysisStandard to set
     */
    public void setAnalysisStandard(AnalysisStandard analysisStandard) {
        this.analysisStandard = analysisStandard;
    }

    /**
     * @return the analysisType
     */
    public String getAnalysisType() {
        return analysisType;
    }

    /**
     * @param analysisType the analysisType to set
     */
    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    /**
     * @return the conversation
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * @return the analysisDeletable
     */
    public boolean isAnalysisDeletable() {
        return analysisDeletable;
    }

    /**
     * @return the entityManagerDAO
     */
    public EntityManagerDao getEntityManagerDAO() {
        return entityManagerDAO;
    }

}
