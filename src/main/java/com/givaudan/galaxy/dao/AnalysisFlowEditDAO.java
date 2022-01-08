package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.exception.NoEntityException;
import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.AnalysisFlow;
import com.givaudan.galaxy.model.core.Notification;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.enums.GivaudanLocationEnum;
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

public class AnalysisFlowEditDAO implements Serializable {

    private static final Logger logger = Logger.getLogger(AnalysisFlowEditDAO.class.getName());

    @Inject
    private EntityManagerDao entityManagerDAO;

    @Inject
    private Identity identity;

    @Inject
    private Conversation conversation;

    private Long id;
    private Long idAnalysisFlow;

    private AnalysisFlow instance;
    private Product product;
    private AnalysisContainer container;

    @PreDestroy
    public void cleanObject() {
        instance = null;
        product = null;
        container = null;
    }
    
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

                String message = new MessageProvider().getValue("module", "analysisFlow_updated");
                String messageInfo = product.getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "MOD");

            } catch (ConstraintViolationException cve) {

                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Analysis flow is not updated.", "");
                conversation.end();
                return "error";
            }
        } else {

            logger.info("create called.");
            try {

                instance.setAnalysisContainer(container);
                container.getAnalysisFlowList().add(instance);
                instance.setLocation(GivaudanLocationEnum.VERNIER);
                entityManagerDAO.createObject(instance);

                String message = new MessageProvider().getValue("module", "analysisFlow_created");
                String messageInfo = product.getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "ADD");

            } catch (ConstraintViolationException cve) {
                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Analysis flow is not created.", "");
                conversation.end();
                return "error";
            }
        }

        conversation.end();
        return "saved";
    }

    public String delete() {
        logger.info("delete called.");

        container.getAnalysisFlowList().remove(instance);
        entityManagerDAO.deleteObject(instance);

        String message = new MessageProvider().getValue("module", "analysisFlow_deleted");
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

    public AnalysisFlow getInstance() {

        if (instance == null) {
            try {

                CriteriaBuilder cb = entityManagerDAO.getEntityManager().getCriteriaBuilder();
                CriteriaQuery<AnalysisFlow> query = cb.createQuery(AnalysisFlow.class);
                Root<AnalysisFlow> object = query.from(AnalysisFlow.class);
                query.where(cb.equal(object.get("id"), idAnalysisFlow));
                instance = entityManagerDAO.getEntityManager().createQuery(query).getSingleResult();

            } catch (NoResultException nre) {

                instance = new AnalysisFlow();
            }
        }

        return instance;
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
     * @return the idAnalysisFlow
     */
    public Long getIdAnalysisFlow() {
        return idAnalysisFlow;
    }

    /**
     * @param idAnalysisFlow the idAnalysisFlow to set
     */
    public void setIdAnalysisFlow(Long idAnalysisFlow) {
        this.idAnalysisFlow = idAnalysisFlow;
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
