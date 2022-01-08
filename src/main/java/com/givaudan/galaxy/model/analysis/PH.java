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
    @NamedQuery(name = "PH.findAllAnalysis", query = "select o from PH o"),
    @NamedQuery(name = "PH.findAnalyisByNumber", query = "select o from PH o where o.number = :number")
})
@XmlRootElement(name = "PH")
@XmlAccessorType(XmlAccessType.FIELD)
public class PH extends Analysis implements Serializable {
    
}
