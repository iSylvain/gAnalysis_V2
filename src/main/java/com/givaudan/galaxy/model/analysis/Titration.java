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
    @NamedQuery(name = "Titration.findAllAnalysis", query = "select o from Titration o"),
    @NamedQuery(name = "Titration.findAnalyisByNumber", query = "select o from Titration o where o.number = :number")
})
@XmlRootElement(name = "titration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Titration extends Analysis implements Serializable {
    
}
