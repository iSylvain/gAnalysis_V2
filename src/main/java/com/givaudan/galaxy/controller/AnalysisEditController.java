package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.AnalysisEditDAO;
import com.givaudan.galaxy.model.AnalysisData;
import com.givaudan.galaxy.model.AnalysisReferenceData;
import com.givaudan.galaxy.model.AnalysisStandard;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
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
public class AnalysisEditController extends AnalysisEditDAO {

    private static final Logger logger = Logger.getLogger(AnalysisGCEditController.class.getName());

    private Product selectedProduct;

    private boolean analysisDataReferenceWired = false;

    //data
    private AnalysisData analysisData = null;

    @Override
    public String save() {

        if (!isManaged()) {
            getInstance().setProduct(selectedProduct);
        }

        getInstance().getDatas().clear();
        getInstance().getDatas().add(analysisData);

        return super.save();
    }

    public String submitPage1() {
        logger.log(Level.INFO, "submitPage1 called CDI: {0}", getConversation().getId());

        setAnalysisStandard(new AnalysisStandard());

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

            String code = selectedProduct.getCode();

            getInstance().setNumber(code + newNumber);

        }

        return "analysisEdit2";
    }

    public String submitPage2() {
        logger.log(Level.INFO, "submitPage2 called CDI: {0}", getConversation().getId());
        return "analysisEdit3";
    }

    public void handleFileUploadReference(FileUploadEvent event) {
        logger.log(Level.INFO, "handleFileUploadReference called");

        AnalysisReferenceData data = new AnalysisReferenceData();
        data.setName(event.getFile().getFileName());
        data.setNumber(getInstance().getNumber());
        data.setDataSize(event.getFile().getSize());
        data.setMime(event.getFile().getContentType());

        try {
            byte[] contents = new byte[(int) data.getDataSize()];
            event.getFile().getInputstream().read(contents);
            data.setContent(contents);
            event.getFile().getInputstream().close();
        } catch (IOException ex) {

        }

        getInstance().setReferenceData(data);
        getInstance().getReferenceData().setAnalysis(getInstance());
        analysisDataReferenceWired = true;

        String msg1 = new MessageProvider().getValue("msg", "downloading");
        String msg2 = new MessageProvider().getValue("module", "reference_uploaded");
        FacesMessage message = new FacesMessage(msg1, MessageFormat.format(msg2, event.getFile().getFileName()));
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void removeReferenceData() {

        logger.log(Level.INFO, "removeReferenceData called");
        analysisDataReferenceWired = false;
        getInstance().getReferenceData().setAnalysis(null);
        getInstance().setReferenceData(null);
        
        String msg1 = new MessageProvider().getValue("module", "reference");
        String msg2 = new MessageProvider().getValue("msg", "file_removed");
        FacesMessage message = new FacesMessage(msg1, msg2);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    /**
     * @return the analysisDataReferenceWired
     */
    public boolean isAnalysisDataReferenceWired() {
        return analysisDataReferenceWired;
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

    /**
     * @return the analysisData
     */
    public AnalysisData getAnalysisData() {
        return analysisData;
    }

    /**
     * @param analysisData the analysisData to set
     */
    public void setAnalysisData(AnalysisData analysisData) {
        this.analysisData = analysisData;
    }

}
