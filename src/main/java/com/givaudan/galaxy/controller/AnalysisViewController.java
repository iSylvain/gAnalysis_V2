package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.AnalysisDAO;
import com.givaudan.galaxy.model.AnalysisData;
import com.givaudan.galaxy.model.analysis.Analysis;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class AnalysisViewController implements Serializable {

    private static final Logger logger = Logger.getLogger(AnalysisViewController.class.getName());

    @Inject
    private AnalysisDAO analysisDAO;

    private Long id;
    private Analysis instance;

    public void load() {

        logger.info("load called");
        instance = analysisDAO.findAnalysisById(id);
    }

    public void openReference() {

        try {
            AnalysisData data = analysisDAO.findAnalysisDataById(instance.getReferenceData().getId());
            logger.log(Level.INFO, "data name : {0}", data.getName());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
//            response.setHeader("Content-Disposition", "attachment; filename=Report.pdf");
            response.setHeader("content-disposition", "inline;filename=Report.pdf");
            response.setContentType("application/pdf");

            try (ServletOutputStream os = response.getOutputStream()) {
                os.write(data.getContent());
                os.flush();
            }
            logger.log(Level.INFO, "facesContext : {0}", facesContext.getExternalContext().getApplicationContextPath());
            facesContext.responseComplete();
            
        } catch (Exception e) {
            logger.log(Level.INFO, "Failure : {0}", e.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void openPDF(AnalysisData data) {
        try {
            logger.log(Level.INFO, "data name : {0}", data.getName());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
//            response.setHeader("Content-Disposition", "attachment; filename=Report.pdf");
            response.setHeader("Content-Disposition", "inline;filename=Report.pdf");
            response.setContentType("application/pdf");

            try (ServletOutputStream os = response.getOutputStream()) {
                os.write(data.getContent());
                os.flush();
                os.close();
            }
            logger.log(Level.INFO, "facesContext : {0}", facesContext.getExternalContext().getApplicationContextPath());
            facesContext.responseComplete();

        } catch (Exception e) {
            logger.log(Level.INFO, "Failure : {0}", e.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public StreamedContent fileDownload(AnalysisData data) {
        InputStream is = new ByteArrayInputStream(data.getContent());
        StreamedContent stream = new DefaultStreamedContent(is, data.getMime(), data.getName());
        return stream;
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
     * @return the instance
     */
    public Analysis getInstance() {
        return instance;
    }

}
