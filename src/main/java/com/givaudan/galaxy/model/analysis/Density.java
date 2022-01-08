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
    @NamedQuery(name = "Density.findAllAnalysis", query = "select o from Density o"),
    @NamedQuery(name = "Density.findAnalyisByNumber", query = "select o from Density o where o.number = :number")
})
@XmlRootElement(name = "density")
@XmlAccessorType(XmlAccessType.FIELD)
public class Density extends Analysis implements Serializable {
    
}
