package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.AnalysisDAO;
import com.givaudan.galaxy.dao.EntityManagerDao;
import com.givaudan.galaxy.dao.ProductDAO;
import com.givaudan.galaxy.exception.NoEntityException;
import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.AnalysisContainerData;
import com.givaudan.galaxy.model.AnalysisReferenceData;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.analysis.Density;
import com.givaudan.galaxy.model.analysis.Divers;
import com.givaudan.galaxy.model.analysis.GC;
import com.givaudan.galaxy.model.analysis.IR;
import com.givaudan.galaxy.model.analysis.KF;
import com.givaudan.galaxy.model.analysis.PH;
import com.givaudan.galaxy.model.analysis.Refraction;
import com.givaudan.galaxy.model.analysis.Titration;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class ProductViewController implements Serializable {

    private static final Logger logger = Logger.getLogger(ProductViewController.class.getName());

    private final String module = "gAnalysis";
    
    @Inject
    private ProductDAO productDAO;

    @Inject
    private AnalysisDAO analysisDAO;

    @Inject
    private EntityManagerDao entityManagerDAO;

    private Long id;
    private AnalysisContainer instance;
    private Product product;

    private Long dataID;

    private Product selectedProduct;

    //Listes d'analyses
    private final List<GC> gcList = new ArrayList<>();
    private final List<Analysis> densityList = new ArrayList<>();
    private final List<Analysis> refractionList = new ArrayList<>();
    private final List<Analysis> titrationList = new ArrayList<>();
    private final List<Analysis> kfList = new ArrayList<>();
    private final List<Analysis> phList = new ArrayList<>();
    private final List<Analysis> irList = new ArrayList<>();
    private final List<Analysis> diversList = new ArrayList<>();

    public void load() {

        logger.info("load called");

        if (product == null) {
            try {
                product = productDAO.findByIdJoinPicture(id);
            } catch (NoResultException e) {
                throw new NoEntityException("No entity Product with id : " + getId());
            }

            if (product.isDataWired()) {
                dataID = product.getPicture().getId();
            }
        }
        
        if (instance == null) {
            try {
                instance = instance = (AnalysisContainer) entityManagerDAO.getEntityManager().createNamedQuery("AnalysisContainer.findByProductId").setParameter("id", getId()).getSingleResult();
                initAnalysisList();
            } catch (NoResultException e) {
                throw new NoEntityException("No entity Container with id : " + getId());
            }
        }
    }

    public String badgeType(String type) {

//        logger.log(Level.INFO, "badgeType called : {0}", type);
        switch (type) {
            case "PRODUCT":
                return "uk-badge-success";
            case "MIXTURE":
                return "uk-badge-warning";
            case "NOTDEFINED":
                return "uk-badge-danger";
            default:
                return "uk-badge-success";
        }
    }

    /**
     * Go to product view in module
     * @param goToModule 
     */
    public void goToModule(String goToModule) {

        try {
//            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//            HttpServletRequest request = (HttpServletRequest) context.getRequest();
//            StringBuffer restOfTheUrl = (StringBuffer) request.getRequestURL();
//            String url = restOfTheUrl.toString();
//            url = url.replace(module, goToModule);
//            context.redirect(url + "?id=" + product.getId());

            String moduleURL = (String) entityManagerDAO.getEntityManager().createNativeQuery("SELECT o.value FROM galaxy_settings o where o.key =:module").setParameter("module", goToModule).getSingleResult();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(moduleURL + "productView.xhtml?id=" + product.getId());
            
        } catch (IOException ex) {
            Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean ifToggleable() {
//        return instance.getProduct().getItems().size() > 6;
        return true;
    }

    public void selectProduct(Product product) {
        logger.info("selectProduct called");
        selectedProduct = product;
    }

    public void openReference(GC analysis) {

        try {
            AnalysisReferenceData data = analysisDAO.findAnalysisReferenceDataByAnalysisID(analysis.getId());
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

    public void openPDF(AnalysisContainerData data) {

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

    public StreamedContent fileDownload(AnalysisContainerData data) {
        InputStream is = new ByteArrayInputStream(data.getContent());
        StreamedContent stream = new DefaultStreamedContent(is, data.getMime(), data.getName());
        return stream;
    }

    private void initAnalysisList() {

        logger.info("initAnalysisList called");

        //on reinitialise toutes les collections d'analyses pour le produit
        gcList.clear();
        densityList.clear();
        refractionList.clear();
        titrationList.clear();
        kfList.clear();
        phList.clear();
        irList.clear();
        diversList.clear();

        if (!instance.getAnalysisList().isEmpty()) {

            for (Analysis a : instance.getAnalysisList()) {

                if (a instanceof GC) {
                    gcList.add((GC) a);
                    continue;
                }
                if (a instanceof Density) {
                    densityList.add((Density) a);//si analyse Density on ajoute l'analyse dans la collection correspondante
                    continue;
                }
                if (a instanceof Refraction) {
                    refractionList.add((Refraction) a);//si analyse Refraction on ajoute l'analyse dans la collection correspondante
                    continue;
                }
                if (a instanceof Titration) {
                    titrationList.add((Titration) a);//si analyse Titration on ajoute l'analyse dans la collection correspondante
                    continue;
                }
                if (a instanceof KF) {
                    kfList.add((KF) a);//si analyse KF on ajoute l'analyse dans la collection correspondante
                    continue;
                }
                if (a instanceof PH) {
                    phList.add((PH) a);//si analyse PH on ajoute l'analyse dans la collection correspondante
                    continue;
                }
                if (a instanceof IR) {
                    irList.add((IR) a);//si analyse PH on ajoute l'analyse dans la collection correspondante
                    continue;
                }
                if (a instanceof Divers) {
                    diversList.add((Divers) a);//si analyse Divers on ajoute l'analyse dans la collection correspondante
                }
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
     * @return the instance
     */
    public AnalysisContainer getInstance() {
        return instance;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @return the gcList
     */
    public List<GC> getGcList() {
        return gcList;
    }

    /**
     * @return the densityList
     */
    public List<Analysis> getDensityList() {
        return densityList;
    }

    /**
     * @return the refractionList
     */
    public List<Analysis> getRefractionList() {
        return refractionList;
    }

    /**
     * @return the titrationList
     */
    public List<Analysis> getTitrationList() {
        return titrationList;
    }

    /**
     * @return the kfList
     */
    public List<Analysis> getKfList() {
        return kfList;
    }

    /**
     * @return the phList
     */
    public List<Analysis> getPhList() {
        return phList;
    }

    /**
     * @return the irList
     */
    public List<Analysis> getIrList() {
        return irList;
    }

    /**
     * @return the diversList
     */
    public List<Analysis> getDiversList() {
        return diversList;
    }

    /**
     * @return the dataID
     */
    public Long getDataID() {
        return dataID;
    }

    /**
     * @return the selectedProduct
     */
    public Product getSelectedProduct() {
        return selectedProduct;
    }

}
