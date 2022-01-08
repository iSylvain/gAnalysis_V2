package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.ContainerEditDAO;
import com.givaudan.galaxy.dao.ProductDAO;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.authorization.annotations.RolesAllowed;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@ConversationScoped
@RolesAllowed("manager")
public class ContainerEditController extends ContainerEditDAO {

    private static final Logger logger = Logger.getLogger(ContainerEditController.class.getName());

    @Inject
    private ProductDAO productDAO;

    private final int nbItems = 25;
    private LazyDataModel<Product> lazyModel;

    public String submitPage1() {
        logger.log(Level.INFO, "submitPage1 called CDI: {0}", getConversation().getId());
        return "containerEdit2";
    }

    public void itemsChanged(String action) {
        logger.log(Level.INFO, "itemsChanged called: {0}", action);
        logger.log(Level.INFO, "getSelectedProductList().size(): {0}", getSelectedProductList().size());
    }

    public void removeItem(Product item) {
        logger.log(Level.INFO, "removeItem called");
        
        //check si produit utilisé pour une analyse
        List<Analysis> analysisList = getEntityManagerDAO().getEntityManager().createNamedQuery("Analysis.findAllAnalysisByProductId").setParameter("id", item.getId()).getResultList();

        if (analysisList != null && analysisList.size() > 0) {
            String message = new MessageProvider().getValue("msg", "product_notDeleted");
            String messageInfo = "Analysis exist";
            MessageUtil.addSuccessMessage(message, messageInfo);
        } else {
            item.setgAnalysis(false);
            item.setAnalysisContainer(null);
            getInstance().getProductList().remove(item);
        }
    }

    /**
     * @return the lazyModel
     */
    public LazyDataModel<Product> getLazyModel() {

        if (lazyModel == null) {

            lazyModel = new LazyDataModel<Product>() {

                @Override
                public Product getRowData(String rowKey) {
                    for (Product product : lazyModel) {
                        if (product.getCode().equals(rowKey)) {
                            return product;
                        }
                    }

                    return null;
                }

                @Override
                public Object getRowKey(Product product) {
                    return product.getCode();
                }

                @Override
                public List<Product> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    int nbProducts = productDAO.countProductInModule(filters, false);
                    logger.log(Level.INFO, "nbProducts: {0}", nbProducts);
                    lazyModel.setRowCount(nbProducts);
                    return productDAO.findProductInModule(first, pageSize, sortField, sortOrder, filters, false);
                }
            };
        }

        return lazyModel;
    }

    /**
     * @return the nbItems
     */
    public int getNbItems() {
        return nbItems;
    }

}
