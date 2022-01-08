package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.core.Data;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "synthesisData")
@XmlAccessorType(XmlAccessType.FIELD)
public class SynthesisData extends Data implements Serializable {
 
}