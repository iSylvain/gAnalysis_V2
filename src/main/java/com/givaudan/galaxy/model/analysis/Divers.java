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
    @NamedQuery(name = "Divers.findAllAnalysis", query = "select o from Divers o"),
    @NamedQuery(name = "Divers.findAnalyisByNumber", query = "select o from Divers o where o.number = :number")
})
@XmlRootElement(name = "divers")
@XmlAccessorType(XmlAccessType.FIELD)
public class Divers extends Analysis implements Serializable {
    
}
