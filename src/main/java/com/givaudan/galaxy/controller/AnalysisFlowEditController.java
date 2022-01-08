package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.AnalysisFlowEditDAO;
import com.givaudan.galaxy.model.AnalysisForFlow;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import org.picketlink.authorization.annotations.RolesAllowed;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.ReorderEvent;

@Named
@ConversationScoped
@RolesAllowed({"manager", "moduleEditor"})
public class AnalysisFlowEditController extends AnalysisFlowEditDAO {

    private static final Logger logger = Logger.getLogger(AnalysisGCEditController.class.getName());

    private Product selectedProduct;

    private boolean ifanalysisForFlowListUpdated = false;
    private List<AnalysisForFlow> analysisForFlowListAvailable = new ArrayList<>();
    private List<AnalysisForFlow> analysisForFlowListSelected = new ArrayList<>();

    @PreDestroy
    public void cleanObject() {
        analysisForFlowListAvailable = null;
        analysisForFlowListSelected = null;
    }
    
    @Override
    public void initConversation() {

        logger.log(Level.INFO, "initConversation called");
        logger.log(Level.INFO, "AnalysisFlow id: {0}", getInstance().getId());

        if (getConversation().isTransient()) {

            logger.log(Level.INFO, "conversation transient");

            super.initConversation();

            getInstance();

            getContainer().getAnalysisList().stream().map((a) -> {
                AnalysisForFlow analysisForFlow = new AnalysisForFlow();
                analysisForFlow.setAnalysisFlow(getInstance());
                analysisForFlow.setAnalysis(a);
                return analysisForFlow;
            }).forEach((analysisForFlow) -> {
                analysisForFlowListAvailable.add(analysisForFlow);
            });

            if (isManaged()) {
//                logger.log(Level.INFO, "***analysisForFlowList().size(): {0}", getInstance().getAnalysisForFlowList().size());
                int position = 0;
                for (AnalysisForFlow analysisForFlow : getInstance().getAnalysisForFlowList()) {
                    analysisForFlow.setPosition(position);
                    analysisForFlowListSelected.add(analysisForFlow);
                    position++;
                }
                Collections.sort(analysisForFlowListSelected);

                getAnalysisForFlowListSelected().stream().forEach((_analysisForFlow) -> {
                    logger.log(Level.INFO, "***analysis step: {0} position: {1}", new Object[]{_analysisForFlow.getProcessStepNumber(), _analysisForFlow.getPosition()});
                });
            }

        }
    }

    @Override
    public String save() {

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

        if (ifanalysisForFlowListUpdated) {
            getInstance().getAnalysisForFlowList().clear();
            analysisForFlowListSelected.stream().forEach((analysisForFlow) -> {
//                logger.log(Level.INFO, "***analysis number: {0} position: {1}", new Object[]{analysisForFlow.getAnalysis().getNumber(), analysisForFlow.getPosition()});
                if (!analysisForFlow.processStepNumberWired()) {
                    analysisForFlow.setProcessStepNumber("-");
                }
                getInstance().getAnalysisForFlowList().add(analysisForFlow);
            });
        }

        return super.save();
    }

    public String submitPage1() {
        logger.log(Level.INFO, "submitPage1 called CDI: {0}", getConversation().getId());
        return "analysisFlowEdit2";
    }

    public String submitPage2() {
        logger.log(Level.INFO, "submitPage2 called CDI: {0}", getConversation().getId());
        ifanalysisForFlowListUpdated = true;
        getAnalysisForFlowListSelected().stream().forEach((_analysisForFlow) -> {
            logger.log(Level.INFO, "***analysis number: {0} position: {1}", new Object[]{_analysisForFlow.getAnalysis().getNumber(), _analysisForFlow.getPosition()});
        });
        return "analysisFlowEdit3";
    }

    public void onDrop(DragDropEvent event) {
        logger.log(Level.INFO, "onDrop called");
        AnalysisForFlow analysisForFlow = ((AnalysisForFlow) event.getData());
        AnalysisForFlow newAnalysisForFlow = new AnalysisForFlow();
        newAnalysisForFlow.setAnalysis(analysisForFlow.getAnalysis());
        newAnalysisForFlow.setAnalysisFlow(getInstance());
        newAnalysisForFlow.setPosition(analysisForFlowListSelected.size());
        ifanalysisForFlowListUpdated = analysisForFlowListSelected.add(newAnalysisForFlow);
//        logger.log(Level.INFO, "analysisForFlowListSelected.size(): {0}", analysisForFlowListSelected.size());
    }

    public void onRowReorder(ReorderEvent event) {
        logger.log(Level.INFO, "onRowReorder called");

        List<AnalysisForFlow> list = new LinkedList<>();
        list.addAll(analysisForFlowListSelected);
        Collections.sort(list);

        list.stream().forEach((_analysisForFlow) -> {
            logger.log(Level.INFO, "***analysis step: {0} position: {1}", new Object[]{_analysisForFlow.getProcessStepNumber(), _analysisForFlow.getPosition()});
        });

        int fromIndex = event.getFromIndex();
        int toIndex = event.getToIndex();
        logger.log(Level.INFO, "***move from {0} to {1}", new Object[]{fromIndex, toIndex});

        AnalysisForFlow analysisForFlow = list.remove(fromIndex);
        list.add(toIndex, analysisForFlow);

        analysisForFlowListSelected.clear();

        int position = 0;
        for (AnalysisForFlow _analysisForFlow : list) {
            _analysisForFlow.setPosition(position);
            analysisForFlowListSelected.add(_analysisForFlow);
            position++;
        }
        Collections.sort(analysisForFlowListSelected);

        ifanalysisForFlowListUpdated = true;

        analysisForFlowListSelected.stream().forEach((_analysisForFlow) -> {
            logger.log(Level.INFO, "***analysis step: {0} position: {1}", new Object[]{_analysisForFlow.getProcessStepNumber(), _analysisForFlow.getPosition()});
        });
    }

    public void deleteAnalysisForFlow(AnalysisForFlow analysisForFlow) {
        logger.log(Level.INFO, "deleteAnalysisForFlow called");
        analysisForFlow.setAnalysisFlow(null);
        analysisForFlowListSelected.remove(analysisForFlow);
        
        int position = 0;
        for (AnalysisForFlow _analysisForFlow : analysisForFlowListSelected) {
            _analysisForFlow.setPosition(position);
            position++;
        }
        Collections.sort(analysisForFlowListSelected);

        ifanalysisForFlowListUpdated = true;
    }

    public boolean analysisForFlowListSelectedNoEmpty() {
        return (analysisForFlowListSelected != null && !analysisForFlowListSelected.isEmpty());
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
     * @return the analysisForFlowListAvailable
     */
    public List<AnalysisForFlow> getAnalysisForFlowListAvailable() {
        return analysisForFlowListAvailable;
    }

    /**
     * @param analysisForFlowListAvailable the analysisForFlowListAvailable to
     * set
     */
    public void setAnalysisForFlowListAvailable(List<AnalysisForFlow> analysisForFlowListAvailable) {
        this.analysisForFlowListAvailable = analysisForFlowListAvailable;
    }

    /**
     * @return the analysisForFlowListSelected
     */
    public List<AnalysisForFlow> getAnalysisForFlowListSelected() {
        return analysisForFlowListSelected;
    }

    /**
     * @param analysisForFlowListSelected the analysisForFlowListSelected to set
     */
    public void setAnalysisForFlowListSelected(List<AnalysisForFlow> analysisForFlowListSelected) {
        this.analysisForFlowListSelected = analysisForFlowListSelected;
    }

}
