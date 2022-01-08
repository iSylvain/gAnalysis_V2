package com.givaudan.galaxy.model.analysis;

import com.givaudan.galaxy.model.core.AnalysisContainer;
import com.givaudan.galaxy.model.AnalysisData;
import com.givaudan.galaxy.model.AnalysisReferenceData;
import com.givaudan.galaxy.model.AnalysisStandard;
import com.givaudan.galaxy.model.core.Product;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "analysis_analysis")
@NamedQueries({
    @NamedQuery(name = "Analysis.findById", query = "select o from Analysis o where o.id = :id"),
    @NamedQuery(name = "Analysis.findAllAnalysis", query = "select o from Analysis o"),
    @NamedQuery(name = "Analysis.findAnalysisByNumber", query = "select o from Analysis o where o.number = :number"),
    @NamedQuery(name = "Analysis.findAllAnalysisByTypeOfAnalysis", query = "select o from Analysis o where o.typeOfAnalysis = :typeOfAnalysis"),
    @NamedQuery(name = "Analysis.findAnalysisByContainerId", query = "select o from Analysis o where o.analysisContainer.id = :id"),
    @NamedQuery(name = "Analysis.findAllAnalysisByProductId", query = "select o from Analysis o where o.product.id = :id"),
    @NamedQuery(name = "Analysis.findAnalyisByProductCode", query = "select o from Analysis o where o.product.code = :code")
})
@XmlRootElement(name = "analysis")
@XmlAccessorType(XmlAccessType.FIELD)
public class Analysis implements Serializable, Comparable<Object> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "analysisID")
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(length = 255, unique = true, updatable = false)
    private String number;

    @Column(length = 255, unique = false, updatable = false)
    @NotNull
    private String typeOfAnalysis;

    @Column(length = 255)
    @NotNull(message = "#{module.summary_null}")
    @Pattern(regexp = ".*[^\\s].*", message = "#{msg.onlySpace}")
    private String summary;

    @Lob
    private String analysisInfo;

    @Transient
    public boolean analysisInfoWired() {
        return (analysisInfo != null && !analysisInfo.isEmpty());
    }

    @Lob
    private String calculation;

    @Transient
    public boolean calculationWired() {
        return (calculation != null && !calculation.isEmpty());
    }

    @Lob
    private String preparation;

    @Transient
    public boolean preparationWired() {
        return (preparation != null && !preparation.isEmpty());
    }

    @Lob
    private String appearanceSample;

    @Transient
    public boolean appearanceSampleWired() {
        return (appearanceSample != null && !appearanceSample.isEmpty());
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analysisContainer", foreignKey = @ForeignKey(name = "fk_analysisContainerForAnalysis"))
    @XmlTransient
    private AnalysisContainer analysisContainer;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "analysis")
    @XmlTransient
    private AnalysisReferenceData referenceData;

    @Transient
    public boolean isReferenceDataWired() {
        return referenceData != null;
    }

    @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @XmlTransient
    private List<AnalysisData> datas = new LinkedList<>();

    @Transient
    public boolean isDatasWired() {
        return datas != null && !datas.isEmpty();
    }

    @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("name ASC")
    @XmlElementWrapper(name = "standards")
    @XmlElement(name = "standard")
    private List<AnalysisStandard> standards = new LinkedList<>();

    @Transient
    public boolean isStandardsWired() {
        return standards != null && !standards.isEmpty();
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "fk_productForAnalysis"), nullable = false)
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
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the typeOfAnalysis
     */
    public String getTypeOfAnalysis() {
        return typeOfAnalysis;
    }

    /**
     * @param typeOfAnalysis the typeOfAnalysis to set
     */
    public void setTypeOfAnalysis(String typeOfAnalysis) {
        this.typeOfAnalysis = typeOfAnalysis;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the analysisInfo
     */
    public String getAnalysisInfo() {
        return analysisInfo;
    }

    /**
     * @param analysisInfo the analysisInfo to set
     */
    public void setAnalysisInfo(String analysisInfo) {
        this.analysisInfo = analysisInfo;
    }

    /**
     * @return the calculation
     */
    public String getCalculation() {
        return calculation;
    }

    /**
     * @param calculation the calculation to set
     */
    public void setCalculation(String calculation) {
        this.calculation = calculation;
    }

    /**
     * @return the preparation
     */
    public String getPreparation() {
        return preparation;
    }

    /**
     * @param preparation the preparation to set
     */
    public void setPreparation(String preparation) {
        this.preparation = preparation;
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

    @Override
    public int compareTo(Object t) {
        int nombre1 = Integer.parseInt(((Analysis) t).getNumber());
        int nombre2 = Integer.parseInt(this.getNumber());
        if (nombre1 > nombre2) {
            return -1;
        } else if (nombre1 == nombre2) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Analysis)) {
            return false;
        }
        Analysis other = (Analysis) object;
        return !((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id)));
    }

    /**
     * @return the datas
     */
    public List<AnalysisData> getDatas() {
        return datas;
    }

    /**
     * @param datas the datas to set
     */
    public void setDatas(List<AnalysisData> datas) {
        this.datas = datas;
    }

    /**
     * @return the standards
     */
    public List<AnalysisStandard> getStandards() {
        return standards;
    }

    /**
     * @param standards the standards to set
     */
    public void setStandards(List<AnalysisStandard> standards) {
        this.standards = standards;
    }

    /**
     * @return the referenceData
     */
    public AnalysisReferenceData getReferenceData() {
        return referenceData;
    }

    /**
     * @param referenceData the referenceData to set
     */
    public void setReferenceData(AnalysisReferenceData referenceData) {
        this.referenceData = referenceData;
    }

    /**
     * @return the appearanceSample
     */
    public String getAppearanceSample() {
        return appearanceSample;
    }

    /**
     * @param appearanceSample the appearanceSample to set
     */
    public void setAppearanceSample(String appearanceSample) {
        this.appearanceSample = appearanceSample;
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

}
