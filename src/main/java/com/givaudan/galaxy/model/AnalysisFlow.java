package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.core.Product;
import com.givaudan.galaxy.model.enums.GivaudanLocationEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OrderColumn;
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

@Entity
@Table(name = "analysis_analysisFlow")
@NamedQueries({
    @NamedQuery(name = "AnalysisFlow.findAllAnalysisFlow", query = "select o from AnalysisFlow o"),
    @NamedQuery(name = "AnalysisFlow.findAnalysisFlowById", query = "select o from AnalysisFlow o where o.id = :id"),
    @NamedQuery(name = "AnalysisFlow.findAllAnalysisFlowByProcessCode", query = "select o from AnalysisFlow o where o.processCode = :processCode")
})
@XmlRootElement(name = "analysisFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisFlow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "analysisFlowID")
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(length = 255)
    @NotNull(message = "processCode_null")
    @Pattern(regexp = ".*[^\\s].*", message = "onlySpace")
    private String processCode;

    private Integer processVersion;

    @Column(length = 25)
    private String operation;
    
    @Transient
    public boolean operationWired() {
        return (operation != null && !operation.isEmpty());
    }

    @Lob
    private String information;

    @Transient
    public boolean informationWired() {
        return (information != null && !information.isEmpty());
    }

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private GivaudanLocationEnum location;

    @Column(length = 25)
    private String locationDetail;
    
    @Transient
    public boolean locationDetailWired() {
        return (locationDetail != null && !locationDetail.isEmpty());
    }

    @OneToMany(mappedBy = "analysisFlow", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name="position")
    @XmlElementWrapper(name = "analysisForFlowList")
    @XmlElement(name = "analysisForFlow")
    private List<AnalysisForFlow> analysisForFlowList = new ArrayList<>();

    @Transient
    public boolean isAnalysisForFlowListWired() {
        return analysisForFlowList != null && !analysisForFlowList.isEmpty();
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_analysisContainer", foreignKey = @ForeignKey(name = "fk_analysisContainerForAnalysisFlow"))
    private AnalysisContainer analysisContainer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "fk_productForAnalysisFlow"), nullable = false)
    private Product product;

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
     * @return the processCode
     */
    public String getProcessCode() {
        return processCode;
    }

    /**
     * @param processCode the processCode to set
     */
    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    /**
     * @return the processVersion
     */
    public Integer getProcessVersion() {
        return processVersion;
    }

    /**
     * @param processVersion the processVersion to set
     */
    public void setProcessVersion(Integer processVersion) {
        this.processVersion = processVersion;
    }

    /**
     * @return the locationDetail
     */
    public String getLocationDetail() {
        return locationDetail;
    }

    /**
     * @param locationDetail the locationDetail to set
     */
    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
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
     * @return the analysisForFlowList
     */
    public List<AnalysisForFlow> getAnalysisForFlowList() {
        return analysisForFlowList;
    }

    /**
     * @param analysisForFlowList the analysisForFlowList to set
     */
    public void setAnalysisForFlowList(List<AnalysisForFlow> analysisForFlowList) {
        this.analysisForFlowList = analysisForFlowList;
    }
}
