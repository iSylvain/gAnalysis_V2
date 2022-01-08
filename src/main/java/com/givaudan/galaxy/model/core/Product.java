package com.givaudan.galaxy.model.core;

import com.givaudan.galaxy.model.enums.MaterialTypeEnum;
import com.givaudan.galaxy.model.enums.ProductTypeEnum;
import com.givaudan.galaxy.util.MessageProvider;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "core_product")
@NamedQueries({
        @NamedQuery(name = "Product.findAll", query = "SELECT o FROM Product o"),
        @NamedQuery(name = "Product.findAllProductLab", query = "SELECT o FROM Product o WHERE o.code LIKE 'LAB%'"),
        @NamedQuery(name = "Product.findById", query = "SELECT o FROM Product o WHERE o.id = :id"),
        @NamedQuery(name = "Product.findByName", query = "SELECT o FROM Product o WHERE o.name_en = :name OR o.name_fr = :name OR o.name_es = :name OR o.name_de = :name"),
        @NamedQuery(name = "Product.findByCode", query = "SELECT o FROM Product o WHERE o.code = :code"),
        @NamedQuery(name = "Product.findByCAS", query = "SELECT o FROM Product o WHERE o.cas = :cas"),
        @NamedQuery(name = "Product.findByEINECS", query = "SELECT o FROM Product o WHERE o.einecs = :einecs")
})
@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product implements Serializable {

    public Product() {

        this.productType = ProductTypeEnum.NOTDEFINED;

        //modules
        this.gAnalysis = false;
        this.gProducts = false;
        this.gSafety = false;
        this.gLab = false;
        this.gFrog = false;
        this.gSynthesis = false;
        this.gRect = false;
        this.gStorage = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "productID")
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(nullable = false, length = 255, unique = true)
    @NotNull(message = "name_null")
    @Size(min = 3, max = 255)
    private String name_en;

    @Column(length = 255)
    private String name_fr;

    @Column(length = 255)
    private String name_es;

    @Column(length = 255)
    private String name_de;

    @Transient
    private String name_msg;

    @Column(length = 255)
    private String upacName;

    @Column(length = 255)
    private String smile;

    @Column(length = 255)
    private String info;

    @Column(length = 25)
    private String cas;

    @Column(length = 255)
    private String casName;

    @Column(length = 25)
    private String einecs;

    @Column(length = 25)
    private String unNumber;

    @Column(length = 25)
    private String onuNumber;

    @Column(length = 25, unique = true)
    @NotNull(message = "code_null")
    @Size(min = 6, max = 25)
    private String code;

    private boolean activeFragrances;
    private boolean activeFlavours;

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    private ProductTypeEnum productType;

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    private MaterialTypeEnum materialType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_data", foreignKey = @ForeignKey(name = "fk_dataForCoreProduct"))
    @XmlTransient
    private CoreData picture;

    //module
    private boolean gAnalysis;
    private boolean gProducts;
    private boolean gSafety;
    private boolean gFrog;
    private boolean gLab;
    private boolean gSynthesis;
    private boolean gRect;
    private boolean gStorage;

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analysisContainer", foreignKey = @ForeignKey(name = "fk_analysisContainer"))
    private AnalysisContainer analysisContainer;

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_productsContainer", foreignKey = @ForeignKey(name = "fk_productsContainer"))
    private ProductsContainer productsContainer;

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_safetyContainer", foreignKey = @ForeignKey(name = "fk_safetyContainer"))
    private SafetyContainer safetyContainer;

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_synthesisContainer", foreignKey = @ForeignKey(name = "fk_synthesisContainer"))
    private SynthesisContainer synthesisContainer;

//    @XmlTransient
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_rectContainer", foreignKey = @ForeignKey(name = "fk_rectContainer"))
//    private RectContainer rectContainer;
//
//    @XmlTransient
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_storageContainer", foreignKey = @ForeignKey(name = "fk_storageContainer"))
//    private StorageContainer storageContainer;

    @Transient
    public boolean name_frWired() {
        return (name_fr != null && !name_fr.isEmpty());
    }

    @Transient
    public boolean name_esWired() {
        return (name_es != null && !name_es.isEmpty());
    }

    @Transient
    public boolean name_deWired() {
        return (name_de != null && !name_de.isEmpty());
    }

    @Transient
    public boolean upacNameWired() {
        return (upacName != null && !upacName.isEmpty());
    }

    @Transient
    public boolean unNumberWired() {
        return (getUnNumber() != null && !getUnNumber().isEmpty());
    }

    @Transient
    public boolean onuNumberWired() {
        return (onuNumber != null && !onuNumber.isEmpty());
    }

    @Transient
    public boolean smileWired() {
        return (smile != null && !smile.isEmpty());
    }

    @Transient
    public boolean infoWired() {
        return (info != null && !info.isEmpty());
    }

    @Transient
    public boolean isDataWired() {
        return getPicture() != null;
    }

    @Transient
    public boolean casWired() {
        return (cas != null && !cas.isEmpty());
    }

    @Transient
    public boolean casNameWired() {
        return (casName != null && !casName.isEmpty());
    }

    @Transient
    public boolean einecsWired() {
        return (einecs != null && !einecs.isEmpty());
    }

    @Transient
    public String getDisplayText() {
        return "name : " + getName_en() + ", code : " + getCode();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((getDisplayText() == null) ? 0 : getDisplayText().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Product other = (Product) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @return the name_msg
     */
    public String getName_msg() {
        String locale = new MessageProvider().getLocale();
        switch (locale) {
            case "en":
                if (name_en != null && !name_en.isEmpty()) {
                    return name_en;
                }
            case "fr":
                if (name_fr != null && !name_fr.isEmpty()) {
                    return name_fr;
                } else {
                    return name_en;
                }
            case "es":
                if (name_es != null && !name_es.isEmpty()) {
                    return name_es;
                } else {
                    return name_en;
                }
            case "de":
                if (name_de != null && !name_de.isEmpty()) {
                    return name_de;
                } else {
                    return name_en;
                }
            default:
                return name_en;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_fr() {
        return name_fr;
    }

    public void setName_fr(String name_fr) {
        this.name_fr = name_fr;
    }

    public String getName_es() {
        return name_es;
    }

    public void setName_es(String name_es) {
        this.name_es = name_es;
    }

    public String getName_de() {
        return name_de;
    }

    public void setName_de(String name_de) {
        this.name_de = name_de;
    }

    public void setName_msg(String name_msg) {
        this.name_msg = name_msg;
    }

    public String getUpacName() {
        return upacName;
    }

    public void setUpacName(String upacName) {
        this.upacName = upacName;
    }

    public String getSmile() {
        return smile;
    }

    public void setSmile(String smile) {
        this.smile = smile;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getCasName() {
        return casName;
    }

    public void setCasName(String casName) {
        this.casName = casName;
    }

    public String getEinecs() {
        return einecs;
    }

    public void setEinecs(String einecs) {
        this.einecs = einecs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActiveFragrances() {
        return activeFragrances;
    }

    public void setActiveFragrances(boolean activeFragrances) {
        this.activeFragrances = activeFragrances;
    }

    public boolean isActiveFlavours() {
        return activeFlavours;
    }

    public void setActiveFlavours(boolean activeFlavours) {
        this.activeFlavours = activeFlavours;
    }

    public ProductTypeEnum getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeEnum productType) {
        this.productType = productType;
    }

    public MaterialTypeEnum getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialTypeEnum materialType) {
        this.materialType = materialType;
    }

    public CoreData getPicture() {
        return picture;
    }

    public void setPicture(CoreData picture) {
        this.picture = picture;
    }

    public boolean isgAnalysis() {
        return gAnalysis;
    }

    public void setgAnalysis(boolean gAnalysis) {
        this.gAnalysis = gAnalysis;
    }

    public boolean isgProducts() {
        return gProducts;
    }

    public void setgProducts(boolean gProducts) {
        this.gProducts = gProducts;
    }

    public boolean isgSafety() {
        return gSafety;
    }

    public void setgSafety(boolean gSafety) {
        this.gSafety = gSafety;
    }

    public boolean isgFrog() {
        return gFrog;
    }

    public void setgFrog(boolean gFrog) {
        this.gFrog = gFrog;
    }

    public boolean isgLab() {
        return gLab;
    }

    public void setgLab(boolean gLab) {
        this.gLab = gLab;
    }

    public boolean isgSynthesis() {
        return gSynthesis;
    }

    public void setgSynthesis(boolean gSynthesis) {
        this.gSynthesis = gSynthesis;
    }

    public boolean isgRect() {
        return gRect;
    }

    public void setgRect(boolean gRect) {
        this.gRect = gRect;
    }

    public boolean isgStorage() {
        return gStorage;
    }

    public void setgStorage(boolean gStorage) {
        this.gStorage = gStorage;
    }

    public AnalysisContainer getAnalysisContainer() {
        return analysisContainer;
    }

    public void setAnalysisContainer(AnalysisContainer analysisContainer) {
        this.analysisContainer = analysisContainer;
    }

    public ProductsContainer getProductsContainer() {
        return productsContainer;
    }

    public void setProductsContainer(ProductsContainer productsContainer) {
        this.productsContainer = productsContainer;
    }

    public SafetyContainer getSafetyContainer() {
        return safetyContainer;
    }

    public void setSafetyContainer(SafetyContainer safetyContainer) {
        this.safetyContainer = safetyContainer;
    }

    public SynthesisContainer getSynthesisContainer() {
        return synthesisContainer;
    }

    public void setSynthesisContainer(SynthesisContainer synthesisContainer) {
        this.synthesisContainer = synthesisContainer;
    }

    public String getUnNumber() {
        return unNumber;
    }

    public void setUnNumber(String unNumber) {
        this.unNumber = unNumber;
    }

    public String getOnuNumber() {
        return onuNumber;
    }

    public void setOnuNumber(String onuNumber) {
        this.onuNumber = onuNumber;
    }
}
