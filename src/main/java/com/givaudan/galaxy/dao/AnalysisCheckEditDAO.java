package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.exception.NoEntityException;
import com.givaudan.galaxy.model.AnalysisCheck;
import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.core.Notification;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;

public class AnalysisCheckEditDAO implements Serializable {

    private static final Logger logger = Logger.getLogger(AnalysisCheckEditDAO.class.getName());

    @Inject
    private EntityManagerDao entityManagerDAO;

    @Inject
    private Identity identity;

    @Inject
    private Conversation conversation;

    private Long id;
    private Long idAnalysisCheck;

    private AnalysisCheck instance;
    private Product product;
    private AnalysisContainer container;
    
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

                String message = new MessageProvider().getValue("module", "analysisCheck_updated");
                String messageInfo = product.getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "MOD");

            } catch (ConstraintViolationException cve) {

                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Analysis check is not updated.", "");
                conversation.end();
                return "error";
            }
        } else {

            logger.info("create called.");
            try {

                instance.setId(container.getId());
                container.setAnalysisCheck(instance);
                entityManagerDAO.createObject(instance);

                String message = new MessageProvider().getValue("module", "analysisCheck_created");
                String messageInfo = product.getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "ADD");

            } catch (ConstraintViolationException cve) {
                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Analysis check is not created.", "");
                conversation.end();
                return "error";
            }
        }

        conversation.end();
        return "saved";
    }

    public String delete() {
        logger.info("delete called.");

        container.setAnalysisCheck(null);
        entityManagerDAO.deleteObject(instance);

        String message = new MessageProvider().getValue("module", "analysisCheck_deleted");
        String messageInfo = product.getName_en();
        MessageUtil.addSuccessMessage(message, messageInfo);

        conversation.end();

        createNotifciation(message, messageInfo, "DEL");

        return "deleted";
    }

    public String cancel() {
        logger.info("cancel called.");
        conversation.end();
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

    public AnalysisCheck getInstance() {

        if (instance == null) {
            try {
                logger.log(Level.INFO, "idAnalysisCheck : {0}", id);
                CriteriaBuilder cb = entityManagerDAO.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<AnalysisCheck> query = cb.createQuery(AnalysisCheck.class);
                Root<AnalysisCheck> object = query.from(AnalysisCheck.class);
                Fetch<AnalysisCheck, Analysis> analysisList = object.fetch("analysisList", JoinType.LEFT);
                query.where(cb.equal(object.get("id"), id));
                instance = entityManagerDAO.getEntityManager().createQuery(query).getSingleResult();

            } catch (NoResultException nre) {
                instance = new AnalysisCheck();
            }
        }

        return instance;
    }

    /**
     * @return the entityManagerDAO
     */
    public EntityManagerDao getEntityManagerDAO() {
        return entityManagerDAO;
    }

    /**
     * @return the conversation
     */
    public Conversation getConversation() {
        return conversation;
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
     * @return the idAnalysisCheck
     */
    public Long getIdAnalysisCheck() {
        return idAnalysisCheck;
    }

    /**
     * @param idAnalysisCheck the idAnalysisCheck to set
     */
    public void setIdAnalysisCheck(Long idAnalysisCheck) {
        this.idAnalysisCheck = idAnalysisCheck;
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

}
