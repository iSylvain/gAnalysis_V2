package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.enums.PolarityColumnGCEnum;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "analysis_columnGC")
@NamedQueries({
    @NamedQuery(name = "ColumnGC.findAllColumnGC", query = "select o from ColumnGC o"),
    @NamedQuery(name = "ColumnGC.findColumnGCByName", query = "select o from ColumnGC o where o.name = :name"),
    @NamedQuery(name = "ColumnGC.findColumnGCByCode", query = "select o from ColumnGC o where o.code = :code"),
    @NamedQuery(name = "ColumnGC.findColumnsGCByPolarity", query = "select o from ColumnGC o where o.polarity = :polarity")
})
@XmlRootElement(name = "columnGc")
@XmlAccessorType(XmlAccessType.FIELD)
public class ColumnGC implements Serializable {

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

    @Column(length = 255, unique = true, updatable = true)
    @NotNull(message = "code_null")
    @Pattern(regexp = ".*[^\\s].*", message = "onlySpace")
    private String code;

    @Column(nullable = false, precision = 5, scale = 1)
    @NotNull
    private float length;

    @Column(nullable = false, precision = 5, scale = 1)
    @NotNull
    private float diameter;

    @Column(nullable = false, precision = 5, scale = 1)
    @NotNull
    private float film;

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private PolarityColumnGCEnum polarity;

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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the length
     */
    public float getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * @return the diameter
     */
    public float getDiameter() {
        return diameter;
    }

    /**
     * @param diameter the diameter to set
     */
    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    /**
     * @return the film
     */
    public float getFilm() {
        return film;
    }

    /**
     * @param film the film to set
     */
    public void setFilm(float film) {
        this.film = film;
    }

    /**
     * @return the polarity
     */
    public PolarityColumnGCEnum getPolarity() {
        return polarity;
    }

    /**
     * @param polarity the polarity to set
     */
    public void setPolarity(PolarityColumnGCEnum polarity) {
        this.polarity = polarity;
    }

}
