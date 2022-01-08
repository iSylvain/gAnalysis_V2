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
    @NamedQuery(name = "KF.findAllAnalysis", query = "select o from KF o"),
    @NamedQuery(name = "KF.findAnalyisByNumber", query = "select o from KF o where o.number = :number")
})
@XmlRootElement(name = "KF")
@XmlAccessorType(XmlAccessType.FIELD)
public class KF extends Analysis implements Serializable {
    
}
