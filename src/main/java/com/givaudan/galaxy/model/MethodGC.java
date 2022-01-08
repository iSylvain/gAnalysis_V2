package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.enums.MethodGCTypeEnum;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "analysis_methodGC")
@NamedQueries({
    @NamedQuery(name = "MethodGC.findAllMethod", query = "select o from MethodGC o order by o.name"),
    @NamedQuery(name = "MethodGC.findMethodByName", query = "select o from MethodGC o where o.name = :name"),
    @NamedQuery(name = "MethodGC.findAllMethodByType", query = "select o from MethodGC o where o.methodGCType = :methodGCType"),
    @NamedQuery(name = "MethodGC.findAllMethodWithoutType", query = "select o from MethodGC o where o.methodGCType != :methodGCType")
})
@XmlRootElement(name = "method")
@XmlAccessorType(XmlAccessType.FIELD)
public class MethodGC implements Serializable {

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

    @Column(length = 255, unique = true, updatable = true)
    @NotNull(message = "name_null")
    @Pattern(regexp = ".*[^\\s].*", message = "onlySpace")
    private String name;

    @Lob
    private String methodInfo;

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private MethodGCTypeEnum methodGCType;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the methodInfo
     */
    public String getMethodInfo() {
        return methodInfo;
    }

    /**
     * @param methodInfo the methodInfo to set
     */
    public void setMethodInfo(String methodInfo) {
        this.methodInfo = methodInfo;
    }

    /**
     * @return the methodGCType
     */
    public MethodGCTypeEnum getMethodGCType() {
        return methodGCType;
    }

    /**
     * @param methodGCType the methodGCType to set
     */
    public void setMethodGCType(MethodGCTypeEnum methodGCType) {
        this.methodGCType = methodGCType;
    }

}
