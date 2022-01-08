package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.core.Notification;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.io.Serializable;
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

public class DatasEditDAO implements Serializable {
    
    private static final Logger logger = Logger.getLogger(DatasEditDAO.class.getName());

    @Inject
    private EntityManagerDao entityManagerDAO;

    @Inject
    private Identity identity;

    @Inject
    private Conversation conversation;

    private Long id;
    private Long idContainer;
    
    private AnalysisContainer instance;
    private Product product;
    
    @PreDestroy
    public void cleanObject() {
        instance = null;
        product = null;
    }
    
    public boolean isManaged() {
        return instance.getId() != null;
    }

    public void initConversation() {
        logger.info("initConversation called.");
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    public String save() {
        logger.info("save called.");

        if (isManaged()) {

            logger.info("update called.");
            try {

                entityManagerDAO.updateObject(instance);

                String message = new MessageProvider().getValue("module", "analysisContainer_updated");
                String messageInfo = product.getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "MOD");

            } catch (ConstraintViolationException cve) {

                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Analysis Container is not updated.", "");
                conversation.end();
                return "error";
            }
        } else {

            logger.info("create called.");
            try {

                entityManagerDAO.createObject(instance);

                String message = new MessageProvider().getValue("module", "analysisContainer_created");
                String messageInfo = product.getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "ADD");

            } catch (ConstraintViolationException cve) {
                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Analysis Container is not created.", "");
                conversation.end();
                return "error";
            }
        }

        getConversation().end();
        return "saved";
    }

    public String delete() {
        logger.info("delete called.");

        entityManagerDAO.deleteObject(instance);

        String message = new MessageProvider().getValue("module", "analysisContainer_deleted");
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
                "AnalysisContainer",
                type,
                user.getLoginName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                message, messageInfo);
        entityManagerDAO.createObject(notification);
    }

    public AnalysisContainer getInstance() {

        if (instance == null) {
            try {
                CriteriaBuilder cb = getEntityManagerDAO().getEntityManager().getCriteriaBuilder();
                CriteriaQuery<AnalysisContainer> query = cb.createQuery(AnalysisContainer.class);
                Root<AnalysisContainer> object = query.from(AnalysisContainer.class);
                query.where(cb.equal(object.get("id"), idContainer));
                instance = getEntityManagerDAO().getEntityManager().createQuery(query).getSingleResult();
            } catch (NoResultException nre) {
                instance = new AnalysisContainer();
            }
            product = getEntityManagerDAO().find(Product.class, id);
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
     * @return the idContainer
     */
    public Long getIdContainer() {
        return idContainer;
    }

    /**
     * @param idContainer the idContainer to set
     */
    public void setIdContainer(Long idContainer) {
        this.idContainer = idContainer;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

}
