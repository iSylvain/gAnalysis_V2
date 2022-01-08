package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.core.Notification;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;

public class ContainerEditDAO implements Serializable {

    private static Logger logger = Logger.getLogger(ContainerEditDAO.class.getName());

    @Inject
    private EntityManagerDao entityManagerDAO;

    @Inject
    private Identity identity;

    @Inject
    private Conversation conversation;

    private Long id;
    private AnalysisContainer instance;

    private List<Product> selectedProductList = new ArrayList<>();

    public boolean isManaged() {
        return getInstance().getId() != null;
    }

    public void initConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    public String save() {
        logger.info("save called.");
        
        if (isManaged()) {

            logger.info("update");
            try {

                for (Product product : selectedProductList) {
                    product.setgAnalysis(true);
                    product.setAnalysisContainer(getInstance());
                    getInstance().getProductList().add(product);
                }

                if (instance.getProductList() == null || instance.getProductList().isEmpty()) {
                    MessageUtil.addErrorMessage("Product list container is empty", "");
                    return null;
                }

                initNameContainer();
                
                entityManagerDAO.updateObject(instance);

                String message = new MessageProvider().getValue("msg", "container_updated");
                String messageInfo = getInstance().getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "MOD", instance.getProductList().get(0).getId());

            } catch (ConstraintViolationException cve) {
                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Container is not updated", "");
                conversation.end();
                return "error";
            }

        } else {

            logger.info("create");
            try {

                if (getSelectedProductList() == null || getSelectedProductList().isEmpty()) {
                    MessageUtil.addErrorMessage("Product list container is empty", "");
                    return null;
                }

                entityManagerDAO.createObject(instance);

                if (!selectedProductList.isEmpty()) {
                    for (Product product : selectedProductList) {
                        product.setgAnalysis(true);
                        product.setAnalysisContainer(getInstance());
                        getInstance().getProductList().add(product);
                    }
                }

                initNameContainer();
                
                entityManagerDAO.updateObject(instance);

                String message = new MessageProvider().getValue("msg", "container_created");
                String messageInfo = getInstance().getName_en();
                MessageUtil.addSuccessMessage(message, messageInfo);
                createNotifciation(message, messageInfo, "ADD", instance.getProductList().get(0).getId());

                //redirect
                id = instance.getId();

            } catch (ConstraintViolationException cve) {
                logger.severe(cve.getMessage());
                MessageUtil.addErrorMessage("Container is not created.", "");
                conversation.end();
                return "error";
            }
        }

        conversation.end();
        return "saved";
    }

    private void initNameContainer() {
        if (!instance.getProductList().isEmpty()) {
            Product pdt = instance.getProductList().get(0);
            if (instance.getName_en() == null || instance.getName_en().isEmpty()) instance.setName_en(pdt.getName_en());         
            if (instance.getName_fr() == null || instance.getName_fr().isEmpty()) instance.setName_fr(pdt.getName_fr());
            if (instance.getName_es() == null || instance.getName_es().isEmpty()) instance.setName_es(pdt.getName_es());
            if (instance.getName_de() == null || instance.getName_de().isEmpty()) instance.setName_de(pdt.getName_de());
        }
    }
   
    public String delete() {

        logger.info("delete called.");

        Long idProduct = instance.getProductList().get(0).getId();
        
        AnalysisContainer container = getInstance();
        
        for (Product product : container.getProductList()) {
            product.setgAnalysis(false);
            product.setAnalysisContainer(null);
        }
        
        String message = new MessageProvider().getValue("msg", "container_deleted");
        String messageInfo = instance.getName_en();
        MessageUtil.addSuccessMessage(message, messageInfo);
        
        instance.getProductList().clear();
        entityManagerDAO.deleteObject(instance);

        createNotifciation(message, messageInfo, "DEL", idProduct);
        conversation.end();
        return "deleted";
    }

    private void createNotifciation(String message, String messageInfo, String type, Long idProduct) {
        logger.info("createNotifciation called.");
        User user = (User) identity.getAccount();
        Notification notification = new Notification("gAnalysis",
                idProduct,
                "Container",
                type,
                user.getLoginName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                message, messageInfo);
        entityManagerDAO.createObject(notification);
    }

    public String cancel() {
        logger.info("cancel called.");
        conversation.end();
        return "cancelled";
    }

    public AnalysisContainer getInstance() {
        if (instance == null) {
            if (getId() != null) {
                logger.info("load instance");
                instance = (AnalysisContainer) entityManagerDAO.getEntityManager().createNamedQuery("AnalysisContainer.findByProductId").setParameter("id", getId()).getSingleResult();
            } else {
                logger.info("create instance");
                instance = new AnalysisContainer();
            }
        }
        return instance;
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
     * @return the conversation
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * @return the selectedProductList
     */
    public List<Product> getSelectedProductList() {
        return selectedProductList;
    }

    /**
     * @param selectedProductList the selectedProductList to set
     */
    public void setSelectedProductList(List<Product> selectedProductList) {
        this.selectedProductList = selectedProductList;
    }

    /**
     * @return the entityManagerDAO
     */
    public EntityManagerDao getEntityManagerDAO() {
        return entityManagerDAO;
    }

}
