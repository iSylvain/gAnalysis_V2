package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.enums.AnalysisCheckEndEnum;
import com.givaudan.galaxy.model.enums.GivaudanLocationEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "analysis_analysisCheckEnd")
@NamedQueries({
    @NamedQuery(name = "AnalysisCheckEnd.findAllAnalysisCheckEnd", query = "select o from AnalysisCheckEnd o"),
    @NamedQuery(name = "AnalysisCheckEnd.findAnalysisCheckEndById", query = "select o from AnalysisCheckEnd o where o.id = :id"),
    @NamedQuery(name = "AnalysisCheckEnd.findAllAnalysisCheckEndWithProductId", query = "select o from AnalysisCheckEnd o where o.product.id = :id"),
    @NamedQuery(name = "AnalysisCheckEnd.findAnalysisListWithId", query = "select o.analysisList from AnalysisCheckEnd o where o.id = :id"),
    @NamedQuery(name = "AnalysisCheckEnd.findAnalysisCheckEndWithProductCodeAndOperation", query = "select o from AnalysisCheckEnd o where o.product.code = :code and o.operation = :operation")
})
@XmlRootElement(name = "analysisCheckEnd")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisCheckEnd implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "analysisCheckEndID")
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(length = 25)
    @NotNull(message = "operation_null")
    @Pattern(regexp = ".*[^\\s].*", message = "onlySpace")
    private String operation;

    @Lob
    private String information;

    @Transient
    public boolean informationWired() {
        return (information != null && !information.isEmpty());
    }

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    private AnalysisCheckEndEnum analysisCheckEndEnum;

    @Column(length = 255)
    private String productNameStart;
    
    @Transient
    public boolean productNameStartWired() {
        return (productNameStart != null && !productNameStart.isEmpty());
    }

    @Column(length = 25)
    private String productCodeStart;

    @Transient
    public boolean productCodeStartWired() {
        return (productCodeStart != null && !productCodeStart.isEmpty());
    }

    private boolean checkSAP;
    private boolean checkCO;
    private boolean checkAppearance;

    @Lob
    private String informationAnalysis;

    @Transient
    public boolean informationAnalysisWired() {
        return (informationAnalysis != null && !informationAnalysis.isEmpty());
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="id_analysisCheckEnd", foreignKey = @ForeignKey(name = "fk_analysisListForAnalysisCheckEnd"))
    @XmlElementWrapper(name = "analysisList")
    @XmlElement(name = "analysis")
    private Set<Analysis> analysisList = new HashSet<>();

    @Transient
    public boolean isAnalysisListWired() {
        return !analysisList.isEmpty();
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_analysisContainer", foreignKey = @ForeignKey(name = "fk_analysisContinerForAnalysisCheckEnd"))
    private AnalysisContainer analysisContainer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "fk_productForAnalysisCheckEnd"), nullable = false)
    private Product product;

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private GivaudanLocationEnum location;

    @XmlTransient
    @OneToMany(mappedBy = "analysisCheckEnd", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Procedure> procedureList = new ArrayList<>();

    @Transient
    public boolean procedureListWired() {
        return (procedureList != null && !procedureList.isEmpty());
    }

    @PrePersist
    public void initTimeStamps() {
        if (getCreatedOn() == null) {
            setCreatedOn(new Date());
        }
        setModifiedOn(getCreatedOn());
    }

    @PreUpdate
    public void updateTimeStamp() {
        setModifiedOn(new Date());
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
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the information
     */
    public String getInformation() {
        return information;
    }

    /**
     * @param information the information to set
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return the analysisCheckEndEnum
     */
    public AnalysisCheckEndEnum getAnalysisCheckEndEnum() {
        return analysisCheckEndEnum;
    }

    /**
     * @param analysisCheckEndEnum the analysisCheckEndEnum to set
     */
    public void setAnalysisCheckEndEnum(AnalysisCheckEndEnum analysisCheckEndEnum) {
        this.analysisCheckEndEnum = analysisCheckEndEnum;
    }

    /**
     * @return the checkSAP
     */
    public boolean isCheckSAP() {
        return checkSAP;
    }

    /**
     * @param checkSAP the checkSAP to set
     */
    public void setCheckSAP(boolean checkSAP) {
        this.checkSAP = checkSAP;
    }

    /**
     * @return the checkCO
     */
    public boolean isCheckCO() {
        return checkCO;
    }

    /**
     * @param checkCO the checkCO to set
     */
    public void setCheckCO(boolean checkCO) {
        this.checkCO = checkCO;
    }

    /**
     * @return the checkAppearance
     */
    public boolean isCheckAppearance() {
        return checkAppearance;
    }

    /**
     * @param checkAppearance the checkAppearance to set
     */
    public void setCheckAppearance(boolean checkAppearance) {
        this.checkAppearance = checkAppearance;
    }

    /**
     * @return the informationAnalysis
     */
    public String getInformationAnalysis() {
        return informationAnalysis;
    }

    /**
     * @param informationAnalysis the informationAnalysis to set
     */
    public void setInformationAnalysis(String informationAnalysis) {
        this.informationAnalysis = informationAnalysis;
    }

    /**
     * @return the analysisList
     */
    public Set<Analysis> getAnalysisList() {
        return analysisList;
    }

    /**
     * @param analysisList the analysisList to set
     */
    public void setAnalysisList(Set<Analysis> analysisList) {
        this.analysisList = analysisList;
    }

    /**
     * @return the analysisContainer
     */
    public AnalysisContainer getAnalysisContainer() {
        return analysisContainer;
    }

    /**
     * @param analysisContainer the analysisContainer to set
     */
    public void setAnalysisContainer(AnalysisContainer analysisContainer) {
        this.analysisContainer = analysisContainer;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the productNameStart
     */
    public String getProductNameStart() {
        return productNameStart;
    }

    /**
     * @param productNameStart the productNameStart to set
     */
    public void setProductNameStart(String productNameStart) {
        this.productNameStart = productNameStart;
    }

    /**
     * @return the productCodeStart
     */
    public String getProductCodeStart() {
        return productCodeStart;
    }

    /**
     * @param productCodeStart the productCodeStart to set
     */
    public void setProductCodeStart(String productCodeStart) {
        this.productCodeStart = productCodeStart;
    }

    /**
     * @return the location
     */
    public GivaudanLocationEnum getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(GivaudanLocationEnum location) {
        this.location = location;
    }

    /**
     * @return the procedureList
     */
    public List<Procedure> getProcedureList() {
        return procedureList;
    }

    /**
     * @param procedureList the procedureList to set
     */
    public void setProcedureList(List<Procedure> procedureList) {
        this.procedureList = procedureList;
    }

}
