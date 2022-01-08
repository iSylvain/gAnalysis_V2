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
    @NamedQuery(name = "IR.findAllAnalysis", query = "select o from IR o"),
    @NamedQuery(name = "IR.findAnalyisByNumber", query = "select o from IR o where o.number = :number")
})
@XmlRootElement(name = "IR")
@XmlAccessorType(XmlAccessType.FIELD)
public class IR extends Analysis implements Serializable {
    
}
