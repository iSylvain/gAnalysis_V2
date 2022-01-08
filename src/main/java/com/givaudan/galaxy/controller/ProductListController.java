package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.ProductDAO;
import com.givaudan.galaxy.model.core.Product;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@ViewScoped
public class ProductListController implements Serializable {

    private static final Logger logger = Logger.getLogger(ProductListController.class.getName());

    @Inject
    private ProductDAO productDAO;

    private LazyDataModel<Product> lazyModel;
    private Product selectedProduct;

    /**
     * Go to product selected
     *
     * @param event
     */
    public void onRowSelect(SelectEvent event) {
        logger.info("onRowSelect called.");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("productView.xhtml?id=" + ((Product) event.getObject()).getId());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return lazy products list
     *
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
                    lazyModel.setRowCount(productDAO.countProductInModule(filters, true));
                    return productDAO.findProductInModule(first, pageSize, sortField, sortOrder, filters, true);
                }
            };
        }

        return lazyModel;
    }

    /**
     * @param lazyModel the lazyModel to set
     */
    public void setLazyModel(LazyDataModel<Product> lazyModel) {
        this.lazyModel = lazyModel;
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
