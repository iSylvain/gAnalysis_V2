package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.AnalysisCheckEndEditDAO;
import com.givaudan.galaxy.model.Procedure;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.enums.AnalysisCheckEndEnum;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.picketlink.authorization.annotations.RolesAllowed;
import org.primefaces.event.DragDropEvent;

@Named
@ConversationScoped
@RolesAllowed({"manager", "moduleEditor"})
public class AnalysisCheckEndEditController extends AnalysisCheckEndEditDAO {

    private static final Logger logger = Logger.getLogger(AnalysisCheckEndEditController.class.getName());

    private Product selectedProduct;

    private List<Analysis> analysisListAvailable = new ArrayList<>();
    private List<Analysis> analysisListSelected = new ArrayList<>();

    @PreDestroy
    public void cleanObject() {
        analysisListAvailable = null;
        analysisListSelected = null;
    }
    
    private Procedure procedure;

    @Override
    public void initConversation() {

        logger.log(Level.INFO, "initConversation called");

        if (getConversation().isTransient()) {

            logger.log(Level.INFO, "conversation transient");

            super.initConversation();
            
            analysisListAvailable.addAll(getContainer().getAnalysisList());
            for(Analysis a : getInstance().getAnalysisList()) {
                    analysisListAvailable.remove(a);
                    analysisListSelected.add(a);
            }
        }
    }

    @Override
    public String save() {

        if (isManaged()) {
            analysisListSelected.stream().forEach((a) -> {
                getInstance().getAnalysisList().add(a);
            });
            analysisListAvailable.stream().forEach((a) -> {
                getInstance().getAnalysisList().remove(a);
            });
        } else {
            analysisListSelected.stream().forEach((a) -> {
                getInstance().getAnalysisList().add(a);
            });
        }

        return super.save();
    }

    public String submitPage1() {
        logger.log(Level.INFO, "submitPage1 called CDI: {0}", getConversation().getId());

        if (!isManaged()) {
            if (selectedProduct == null) {
                String message = new MessageProvider().getValue("msg", "error");
                String messageInfo = new MessageProvider().getValue("msg", "container_selectProduct_error");
                MessageUtil.addErrorMessage(message, messageInfo);
                return "";
            } else {
                getInstance().setProduct(selectedProduct);
            }
        }

        return "analysisCheckEndEdit2";
    }

    public String submitPage2() {
        logger.log(Level.INFO, "submitPage2 called CDI: {0}", getConversation().getId());
        procedure = new Procedure();
        return "analysisCheckEndEdit3";
    }

    public void addProcedure() {
        logger.log(Level.INFO, "addProcedure called");
        getInstance().getProcedureList().add(procedure);
        procedure.setAnalysisCheckEnd(getInstance());        
        procedure = new Procedure();
    }

    public void deleteProcedure(Procedure procedure) {
        logger.log(Level.INFO, "deleteProcedure called"); 
        procedure.setAnalysisCheckEnd(null);
        getInstance().getProcedureList().remove(procedure);
    }

    public void onDrop(DragDropEvent ddEvent) {
        logger.log(Level.INFO, "onDrop called");
        Analysis event = ((Analysis) ddEvent.getData());
        try {
            getInstance().getAnalysisList().add(event);
            analysisListAvailable.remove(event);
            analysisListSelected.add(event);
        } catch (Exception e) {
            logger.log(Level.INFO, "*** error : {0}", e.getMessage());
        }
    }

    public void deleteAnalysis(Analysis analysis) {
        logger.log(Level.INFO, "deleteAnalysis called");
        getInstance().getAnalysisList().remove(analysis);
        analysisListAvailable.add(analysis);
        analysisListSelected.remove(analysis);
    }

    @Produces
    @Named
    public AnalysisCheckEndEnum[] getAnalysisCheckEndEnum() {
        return AnalysisCheckEndEnum.values();
    }

    /**
     * @return the analysisListAvailable
     */
    public List<Analysis> getAnalysisListAvailable() {
        return analysisListAvailable;
    }

    /**
     * @return the analysisListSelected
     */
    public List<Analysis> getAnalysisListSelected() {
        return analysisListSelected;
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
     * @return the procedure
     */
    public Procedure getProcedure() {
        return procedure;
    }

    /**
     * @param procedure the procedure to set
     */
    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

}
