package com.givaudan.galaxy.model.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries({
    @NamedQuery(name = "ProductsContainer.findAll", query = "SELECT o FROM ProductsContainer o"),
    @NamedQuery(name = "ProductsContainer.findById", query = "SELECT o FROM ProductsContainer o where o.id = :id"),
    @NamedQuery(name = "ProductsContainer.findByProductId", query = "SELECT o.productsContainer FROM Product o where o.id = :id"),
    @NamedQuery(name = "ProductsContainer.findByProductCode", query = "SELECT o.productsContainer FROM Product o where o.code = :code")
})
@Table(name = "products_container")
@XmlRootElement(name = "productsContainer")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductsContainer extends ModuleContainer implements Serializable {
 
    @OneToMany(mappedBy = "productsContainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("name_en ASC")
    @XmlElementWrapper(name = "products")
    @XmlElement(name = "product")
    private List<Product> productList = new LinkedList<>();

    /**
     * @return the productList
     */
    public List<Product> getProductList() {
        return productList;
    }

    /**
     * @param productList the productList to set
     */
    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

}
