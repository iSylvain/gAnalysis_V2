package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.AnalysisCheckEditDAO;
import com.givaudan.galaxy.model.analysis.Analysis;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import org.picketlink.authorization.annotations.RolesAllowed;
import org.primefaces.event.DragDropEvent;

@Named
@ConversationScoped
@RolesAllowed({"manager", "moduleEditor"})
public class AnalysisCheckEditController extends AnalysisCheckEditDAO {

    private static final Logger logger = Logger.getLogger(AnalysisCheckEditController.class.getName());

    private List<Analysis> analysisListAvailable = new ArrayList<>();
    private List<Analysis> analysisListSelected = new ArrayList<>();
    
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
        
        if(isManaged()) {
            for(Analysis a : analysisListSelected) {
                getInstance().getAnalysisList().add(a);
            }
            for(Analysis a : analysisListAvailable) {
                getInstance().getAnalysisList().remove(a);
            }
        } else {
            for(Analysis a : analysisListSelected) {
                getInstance().getAnalysisList().add(a);
            }
        }

        return super.save();
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

}
