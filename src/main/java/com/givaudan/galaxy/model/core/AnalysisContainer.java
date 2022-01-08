package com.givaudan.galaxy.model.core;

import com.givaudan.galaxy.model.AnalysisCheck;
import com.givaudan.galaxy.model.AnalysisCheckEnd;
import com.givaudan.galaxy.model.AnalysisContainerData;
import com.givaudan.galaxy.model.AnalysisFlow;
import com.givaudan.galaxy.model.analysis.Analysis;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "analysis_container")
@NamedQueries({
    @NamedQuery(name = "AnalysisContainer.findAll", query = "SELECT o FROM AnalysisContainer o"),
    @NamedQuery(name = "AnalysisContainer.findById", query = "SELECT o FROM AnalysisContainer o where o.id = :id"),
    @NamedQuery(name = "AnalysisContainer.findByProductId", query = "SELECT o.analysisContainer FROM Product o where o.id = :id"),
    @NamedQuery(name = "AnalysisContainer.findByProductCode", query = "SELECT o.analysisContainer FROM Product o where o.code = :code")
})
@XmlRootElement(name = "analysisContainer")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisContainer extends ModuleContainer implements Serializable {

    @OneToMany(mappedBy = "analysisContainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("name_en ASC")
    @XmlElementWrapper(name = "products")
    @XmlElement(name = "product")
    private List<Product> productList = new LinkedList<>();

    private Long analysisCounter = 0l;
    
    @OneToMany(mappedBy = "analysisContainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("number ASC")
    @XmlElementWrapper(name = "analysisList")
    @XmlElement(name = "analysis")
    private List<Analysis> analysisList = new LinkedList<>();

    @Transient
    public boolean isAnalysisListWired() {
        return !analysisList.isEmpty();
    }

    @OneToMany(mappedBy = "analysisContainer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("processCode ASC")
    @XmlElementWrapper(name = "analysisFlowList")
    @XmlElement(name = "analysisFlow")
    private List<AnalysisFlow> analysisFlowList = new LinkedList<>();

    @Transient
    public boolean isAnalysisFlowListWired() {
        return !analysisFlowList.isEmpty();
    }

    @OneToMany(mappedBy = "analysisContainer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @XmlElementWrapper(name = "analysisCheckEndList")
    @XmlElement(name = "analysisCheckEnd")
    private List<AnalysisCheckEnd> analysisCheckEndList = new LinkedList<>();

    @Transient
    public boolean isAnalysisCheckEndListWired() {
        return !analysisCheckEndList.isEmpty();
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_analysisCheck", foreignKey = @ForeignKey(name = "fk_analysisCheck"))
    private AnalysisCheck analysisCheck;

    @Transient
    public boolean isAnalysisCheckWired() {
        return analysisCheck != null;
    }

    @OneToMany(mappedBy = "analysisContainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @XmlTransient
    private List<AnalysisContainerData> datas = new LinkedList<>();

    @Transient
    public boolean isDatasWired() {
        return datas != null && !datas.isEmpty();
    }

    /**
     * @return the analysisCounter
     */
    public Long getAnalysisCounter() {
        return analysisCounter;
    }

    /**
     * @param analysisCounter the analysisCounter to set
     */
    public void setAnalysisCounter(Long analysisCounter) {
        this.analysisCounter = analysisCounter;
    }

    /**
     * @return the AnalysisList
     */
    public List<Analysis> getAnalysisList() {
        return analysisList;
    }

    /**
     * @param analysisList
     */
    public void setAnalysisList(List<Analysis> analysisList) {
        this.analysisList = analysisList;
    }

    /**
     * @return the analysisFlowList
     */
    public List<AnalysisFlow> getAnalysisFlowList() {
        return analysisFlowList;
    }

    /**
     * @param analysisFlowList the analysisFlowList to set
     */
    public void setAnalysisFlowList(List<AnalysisFlow> analysisFlowList) {
        this.analysisFlowList = analysisFlowList;
    }

    /**
     * @return the analysisCheck
     */
    public AnalysisCheck getAnalysisCheck() {
        return analysisCheck;
    }

    /**
     * @param analysisCheck the analysisCheck to set
     */
    public void setAnalysisCheck(AnalysisCheck analysisCheck) {
        this.analysisCheck = analysisCheck;
    }

    /**
     * @return the datas
     */
    public List<AnalysisContainerData> getDatas() {
        return datas;
    }

    /**
     * @param datas the datas to set
     */
    public void setDatas(List<AnalysisContainerData> datas) {
        this.datas = datas;
    }

    /**
     * @return the analysisCheckEndList
     */
    public List<AnalysisCheckEnd> getAnalysisCheckEndList() {
        return analysisCheckEndList;
    }

    /**
     * @param analysisCheckEndList the analysisCheckEndList to set
     */
    public void setAnalysisCheckEndList(List<AnalysisCheckEnd> analysisCheckEndList) {
        this.analysisCheckEndList = analysisCheckEndList;
    }

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
