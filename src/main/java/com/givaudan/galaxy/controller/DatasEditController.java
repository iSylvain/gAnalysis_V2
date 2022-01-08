package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.DatasEditDAO;
import com.givaudan.galaxy.model.AnalysisContainerData;
import com.givaudan.galaxy.util.MessageProvider;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.picketlink.authorization.annotations.RolesAllowed;
import org.primefaces.event.FileUploadEvent;

@Named
@ConversationScoped
@RolesAllowed({"manager", "moduleEditor"})
public class DatasEditController extends DatasEditDAO {

    private static final Logger logger = Logger.getLogger(DatasEditController.class.getName());

    private int maxFiles = 5;

    public void handleFileUpload(FileUploadEvent event) {
        logger.log(Level.INFO, "handleFileUpload called");

        AnalysisContainerData data = new AnalysisContainerData();
        data.setName(event.getFile().getFileName());
        data.setDataSize(event.getFile().getSize());
        
        if(event.getFile().getContentType().contains("pdf")) {
            data.setMime("pdf");
        } else {
            data.setMime(event.getFile().getContentType());
        }
    
        try {
            byte[] contents = new byte[(int) data.getDataSize()];
            event.getFile().getInputstream().read(contents);
            data.setContent(contents);
            event.getFile().getInputstream().close();
        } catch (IOException ex) {

        }

        data.setAnalysisContainer(getInstance());
        getInstance().getDatas().add(data);

        String msg1 = new MessageProvider().getValue("msg", "downloading");
        String msg2 = new MessageProvider().getValue("msg", "file_uploaded");
        FacesMessage message = new FacesMessage(msg1, MessageFormat.format(msg2, event.getFile().getFileName()));
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void removeData(AnalysisContainerData data) {

        logger.log(Level.INFO, "removeData called");
        data.setAnalysisContainer(null);
        getInstance().getDatas().remove(data);
        
        String msg1 = data.getName();
        String msg2 = new MessageProvider().getValue("msg", "file_removed");
        FacesMessage message = new FacesMessage(msg1, msg2);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public boolean ifMaxFiles() {
        return getInstance().getDatas().size() < maxFiles;
    }

}
