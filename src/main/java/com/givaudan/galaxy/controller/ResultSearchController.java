package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.AnalysisDAO;
import com.givaudan.galaxy.dao.AnalysisServiceDAO;
import com.givaudan.galaxy.dao.ProductDAO;
import com.givaudan.galaxy.model.AnalysisReferenceData;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.util.SearchUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.SelectEvent;

@Named
@SessionScoped
public class ResultSearchController implements Serializable {

    private static final Logger logger = Logger.getLogger(ResultSearchController.class.getName());

    @Inject
    private ProductDAO productDAO;

    @Inject
    private AnalysisServiceDAO analysisServiceDAO;

    @Inject
    private AnalysisDAO analysisDAO;

    /**
     * Return result list with searchString criteria
     *
     * @param searchString
     * @return
     */
    public List<SearchUtil> searchResultList(String searchString) {
        logger.log(Level.INFO, "searchResultList called with query : {0}", searchString);

        if (searchString != null) {
            List<SearchUtil> resultList = new ArrayList<>();

            if (searchString.startsWith("@")) {
                Analysis a = analysisServiceDAO.findAnalysisByNumber(searchString.substring(1));
                if (a != null) {

                    SearchUtil result = new SearchUtil();
                    result.setId(a.getId());
                    result.setText("Analyse");
                    if (a.getTypeOfAnalysis().contains("GC")) {
                        result.setType("gc");
                    } else {
                        result.setType("a");
                    }
                    resultList.add(result);

                    return resultList;
                }
            } else if (searchString.startsWith("$")) {
                AnalysisReferenceData r = analysisServiceDAO.findReferenceByNumber(searchString.substring(1));
                if (r != null) {

                    SearchUtil result = new SearchUtil();
                    result.setId(r.getId());
                    result.setText("Référence");
                    result.setType("r");
                    resultList.add(result);

                    return resultList;
                }
            } else {
                List<Product> productList = productDAO.searchProductWithNameOrCodeInModule(searchString);

                productList.stream().map((product) -> {
                    SearchUtil result = new SearchUtil();
                    result.setId(product.getId());
                    result.setText(product.getName_en() + " " + product.getCode());
                    result.setType("p");
                    return result;
                }).forEach((result) -> {
                    resultList.add(result);
                });

                return resultList;
            }
        }

        return null;
    }

    /**
     * Go to product selected
     *
     * @param event
     */
    public void onResultSelect(SelectEvent event) {
        logger.info("onResultSelect called");

        try {
            SearchUtil result = (SearchUtil) event.getObject();

            switch (result.getType()) {
                case "p":
                    FacesContext.getCurrentInstance().getExternalContext().redirect("productView.xhtml?id=" + result.getId());
                    break;
                case "gc":
                    FacesContext.getCurrentInstance().getExternalContext().redirect("analysisGCView.xhtml?id=" + result.getId());
                    break;
                case "a":
                    FacesContext.getCurrentInstance().getExternalContext().redirect("analysisView.xhtml?id=" + result.getId());
                    break;
                case "r":
                    AnalysisReferenceData data = analysisDAO.findAnalysisReferneceDataById(result.getId());
//                    logger.log(Level.INFO, "data name : {0}", data.getName());
                    
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    facesContext.getExternalContext().responseReset();
                    HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                    response.setHeader("Content-Disposition", "attachment;filename=Report.pdf");
                    response.setContentType("application/pdf");
                    
                    try (ServletOutputStream os = response.getOutputStream()) {
                        os.write(data.getContent());
                        os.flush();
                        os.close();
                    } 
                    
                    logger.log(Level.INFO, "facesContext : {0}", facesContext.getExternalContext().getApplicationContextPath());
                    facesContext.responseComplete();

                    break;
                default:
                    break;
            }

        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

}
