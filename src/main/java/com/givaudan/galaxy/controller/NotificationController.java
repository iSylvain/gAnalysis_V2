package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.NotificationDAO;
import com.givaudan.galaxy.dao.ProductDAO;
import com.givaudan.galaxy.model.core.Notification;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.util.MessageUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class NotificationController implements Serializable {

    private static final Logger logger = Logger.getLogger(NotificationController.class.getName());

    @Inject
    private NotificationDAO notificationDAO;

    @Inject
    private ProductDAO productDAO;

    private List<Notification> notificationList;

    /**
     * Load last modifications in module
     */
    @PostConstruct
    public void load() {
        logger.info("load called");
        notificationList = notificationDAO.findAllWithModuleAndLimit(10);
    }

    /**
     * Go to product notification
     *
     * @param notification
     * @throws IOException
     */
    public void goToProduct(Notification notification) throws IOException {

        Product product = null;

        if (notification.getType().contains("DEL")) return;
        
        switch (notification.getOwnerName()) {

            case "Product":
                product = productDAO.findById(notification.getOwnerID());
                if (product != null) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("productView.xhtml?id=" + notification.getOwnerID());
                } else {
                    MessageUtil.addWarmMessage("No product with id: " + notification.getOwnerID(), null);
                }
                break;
            case "Container":
                product = productDAO.findById(notification.getOwnerID());
                if (product != null) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("productView.xhtml?id=" + notification.getOwnerID());
                } else {
                    MessageUtil.addWarmMessage("No container with id: " + notification.getOwnerID(), null);
                }
                break;
            default:
                MessageUtil.addWarmMessage("No notifacation with id: " + notification.getOwnerID(), null);
        }
    }

    /**
     * Return css attribut for badge
     *
     * @param type
     * @return
     */
    public String badgeType(String type) {

        switch (type) {
            case "ADD":
                return "uk-badge-success";
            case "MOD":
                return "uk-badge-warning";
            case "DEL":
                return "uk-badge-danger";
            default:
                return "uk-badge-success";
        }
    }

    /**
     * @return the notificationList
     */
    public List<Notification> getNotificationList() {
        return notificationList;
    }

}
