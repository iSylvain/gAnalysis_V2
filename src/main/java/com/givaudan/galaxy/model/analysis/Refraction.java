package com.givaudan.galaxy.model.analysis;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
    @NamedQuery(name = "Refraction.findAllAnalysis", query = "select o from Refraction o"),
    @NamedQuery(name = "Refraction.findAnalyisByNumber", query = "select o from Refraction o where o.number = :number")
})
@XmlRootElement(name = "refraction")
@XmlAccessorType(XmlAccessType.FIELD)
public class Refraction extends Analysis implements Serializable {
    
}
