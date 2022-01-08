package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.EntityManagerDao;
import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.analysis.GC;
import com.givaudan.galaxy.util.InformationAnalysisUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class InformationAnalysisViewController implements Serializable {

    private static final Logger logger = Logger.getLogger(InformationAnalysisViewController.class.getName());

    @Inject
    private EntityManagerDao entityManagerDAO;

    private Long id;
    private AnalysisContainer container;
    private List<InformationAnalysisUtil> informationAnalysisList = new LinkedList<>();

    public void load() {

        logger.info("load called");
        container = (AnalysisContainer) entityManagerDAO.getEntityManager().createNamedQuery("AnalysisContainer.findByProductId").setParameter("id", getId()).getSingleResult();

        if (container.isAnalysisListWired()) {
            logger.log(Level.INFO, "analysisList.size: {0}", container.getAnalysisList().size());
            for (Analysis a : container.getAnalysisList()) {

                InformationAnalysisUtil information = new InformationAnalysisUtil();
                information.setNumber(a.getNumber());
                information.setTypeOfAnalysis(a.getTypeOfAnalysis());
                information.setSummary(a.getSummary());
                
                if (a.analysisInfoWired()) {
                    information.setAnalysisInfo(a.getAnalysisInfo());
                }

                if (a instanceof GC) {
                    if (((GC) a).gcInfoWired()) {
                        information.setGcInfo(((GC) a).getGcInfo());
                    }
                    information.setMethodGC(((GC) a).getMethod().getName());
                    information.setProgramGC(((GC) a).getProgram().getName());
                } 
                
                informationAnalysisList.add(information);
            }
        }
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
     * @return the container
     */
    public AnalysisContainer getContainer() {
        return container;
    }

    /**
     * @return the informationAnalysisList
     */
    public List<InformationAnalysisUtil> getInformationAnalysisList() {
        return informationAnalysisList;
    }

}
