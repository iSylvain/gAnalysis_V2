package com.givaudan.galaxy.model.core;

import com.givaudan.galaxy.util.MessageProvider;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@MappedSuperclass
@XmlRootElement(name = "moduleContainer")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ModuleContainer implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Size(max = 255)
    private String name_en;

    @Column(length = 255)
    private String name_fr;

    @Column(length = 255)
    private String name_es;

    @Column(length = 255)
    private String name_de;

    @Transient
    private String name_msg;

    @Lob
    private String containerInformation;

    @Transient
    public boolean informationWired() {
        return (containerInformation != null && !containerInformation.isEmpty());
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
     * @return the name_en
     */
    public String getName_en() {
        return name_en;
    }

    /**
     * @param name_en the name_en to set
     */
    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    /**
     * @return the name_fr
     */
    public String getName_fr() {
        return name_fr;
    }

    /**
     * @param name_fr the name_fr to set
     */
    public void setName_fr(String name_fr) {
        this.name_fr = name_fr;
    }

    /**
     * @return the name_es
     */
    public String getName_es() {
        return name_es;
    }

    /**
     * @param name_es the name_es to set
     */
    public void setName_es(String name_es) {
        this.name_es = name_es;
    }

    /**
     * @return the name_de
     */
    public String getName_de() {
        return name_de;
    }

    /**
     * @param name_de the name_de to set
     */
    public void setName_de(String name_de) {
        this.name_de = name_de;
    }

    /**
     * @return the containerInformation
     */
    public String getContainerInformation() {
        return containerInformation;
    }

    /**
     * @param containerInformation the containerInformation to set
     */
    public void setContainerInformation(String containerInformation) {
        this.containerInformation = containerInformation;
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

}
