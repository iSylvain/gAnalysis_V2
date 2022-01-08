package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.AnalysisGCEditDAO;
import com.givaudan.galaxy.model.AnalysisData;
import com.givaudan.galaxy.model.AnalysisReferenceData;
import com.givaudan.galaxy.model.AnalysisStandard;
import com.givaudan.galaxy.model.MethodGC;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.ProgramGC;
import com.givaudan.galaxy.model.enums.AnalysisGCTypeEnum;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.picketlink.authorization.annotations.RolesAllowed;
import org.primefaces.event.FileUploadEvent;

@Named
@ConversationScoped
@RolesAllowed({"manager", "moduleEditor"})
public class AnalysisGCEditController extends AnalysisGCEditDAO {

    private static final Logger logger = Logger.getLogger(AnalysisGCEditController.class.getName());

    private Product selectedProduct;

    private boolean analysisDataReferenceWired = false;
    private AnalysisReferenceData referenceData;

    private int maxFiles = 5;

    private List<ProgramGC> programGCList;
    private List<MethodGC> methodGCList;

    @PreDestroy
    public void cleanObject() {
        selectedProduct = null;
        referenceData = null;
        programGCList = null;
        methodGCList = null;
    }
    
    @Override
    public void initConversation() {
        logger.info("initConversation called.");

        if (getConversation().isTransient()) {
            programGCList = getEntityManagerDAO().getEntityManager().createNamedQuery("ProgramGC.findAllProgramGC").getResultList();
            methodGCList = getEntityManagerDAO().getEntityManager().createNamedQuery("MethodGC.findAllMethod").getResultList();
        }

        super.initConversation();
    }

    public String save() {

        if (!isManaged()) {
            getInstance().setProduct(selectedProduct);
        }

        return super.save(referenceData);
    }

    public String submitPage1() {
        logger.log(Level.INFO, "submitPage1 called CDI: {0}", getConversation().getId());

        if (!isManaged()) {
            if (selectedProduct == null) {
                String message = new MessageProvider().getValue("msg", "error");
                String messageInfo = new MessageProvider().getValue("msg", "container_selectProduct_error");
                MessageUtil.addErrorMessage(message, messageInfo);
                return "";
            }

            //assignation du numéro d'analyse
            Long newNumber = getContainer().getAnalysisCounter() + 1;
            getContainer().setAnalysisCounter(newNumber);

            if (isForRectification()) {
                getInstance().setNumber(selectedProduct.getCode());
            } else {
                String code = selectedProduct.getCode();
                getInstance().setNumber(code + newNumber);
            }

        }

        setAnalysisStandard(new AnalysisStandard());
        return "analysisGCEdit2";
    }

    public String submitPage2() {
        logger.log(Level.INFO, "submitPage2 called CDI: {0}", getConversation().getId());
        return "analysisGCEdit3";
    }

    public String submitPage3() {
        logger.log(Level.INFO, "submitPage3 called CDI: {0}", getConversation().getId());
        return "analysisGCEdit4";
    }

    @Produces
    @Named
    public AnalysisGCTypeEnum[] getAnalysisGCTypeEnum() {
        return AnalysisGCTypeEnum.values();
    }

    public void handleFileUploadReference(FileUploadEvent event) {
        logger.log(Level.INFO, "handleFileUploadReference called");

        referenceData = new AnalysisReferenceData();
        referenceData.setName(event.getFile().getFileName());
        referenceData.setNumber(getInstance().getNumber());
        referenceData.setDataSize(event.getFile().getSize());
        referenceData.setMime(event.getFile().getContentType());
        referenceData.setAnalysis(getInstance());

        try {
            byte[] contents = new byte[(int) referenceData.getDataSize()];
            event.getFile().getInputstream().read(contents);
            referenceData.setContent(contents);
            event.getFile().getInputstream().close();

            analysisDataReferenceWired = true;

            String msg1 = new MessageProvider().getValue("msg", "downloading");
            String msg2 = new MessageProvider().getValue("module", "reference_uploaded");
            FacesMessage message = new FacesMessage(msg1, MessageFormat.format(msg2, event.getFile().getFileName()));
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (IOException ex) {
            referenceData = null;
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        logger.log(Level.INFO, "handleFileUpload called");

        AnalysisData data = new AnalysisData();
        data.setName(event.getFile().getFileName());
        data.setDataSize(event.getFile().getSize());

        if (event.getFile().getContentType().contains("pdf")) {
            data.setMime("pdf");
        } else {
            data.setMime(event.getFile().getContentType());
        }

        try {
            byte[] contents = new byte[(int) data.getDataSize()];
            event.getFile().getInputstream().read(contents);
            data.setContent(contents);
            event.getFile().getInputstream().close();

            data.setAnalysis(getInstance());
            getInstance().getDatas().add(data);

            String msg1 = new MessageProvider().getValue("msg", "downloading");
            String msg2 = new MessageProvider().getValue("msg", "file_uploaded");
            FacesMessage message = new FacesMessage(msg1, MessageFormat.format(msg2, event.getFile().getFileName()));
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException ex) {
        }
    }

    public void removeReferenceData() {
        logger.log(Level.INFO, "removeReferenceData called");

        analysisDataReferenceWired = false;
        getInstance().setReferenceData(null);

        String msg1 = new MessageProvider().getValue("module", "reference");
        String msg2 = new MessageProvider().getValue("msg", "file_removed");
        FacesMessage message = new FacesMessage(msg1, msg2);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void removeData(AnalysisData data) {
        logger.log(Level.INFO, "removeData called");

        getInstance().getDatas().remove(data);

        String msg1 = data.getName();
        String msg2 = new MessageProvider().getValue("msg", "file_removed");
        FacesMessage message = new FacesMessage(msg1, msg2);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public boolean ifMaxFiles() {
        return getInstance().getDatas().size() < maxFiles;
    }

    /**
     * @return the analysisDataReferenceWired
     */
    public boolean isAnalysisDataReferenceWired() {
        return analysisDataReferenceWired;
    }

    /**
     * @return the maxFiles
     */
    public int getMaxFiles() {
        return maxFiles;
    }

    /**
     * @return the programGCList
     */
    public List<ProgramGC> getProgramGCList() {
        return programGCList;
    }

    /**
     * @return the methodGCList
     */
    public List<MethodGC> getMethodGCList() {
        return methodGCList;
    }

    /**
     * @return the selectedProduct
     */
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct the selectedProduct to set
     */
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

}
