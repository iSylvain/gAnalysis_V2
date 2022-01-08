package com.givaudan.galaxy.model.analysis;

import com.givaudan.galaxy.model.enums.GivaudanLocationEnum;
import com.givaudan.galaxy.model.MethodGC;
import com.givaudan.galaxy.model.ProgramGC;
import com.givaudan.galaxy.model.enums.AnalysisGCTypeEnum;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
        @NamedQuery(name = "GC.findAllAnalysis", query = "select o from GC o"),
        @NamedQuery(name = "GC.findAnalyisByNumber", query = "select o from GC o where o.number = :number"),
        @NamedQuery(name = "GC.findAnalyisByContainerId", query = "select o from GC o where o.analysisContainer.id = :id"),
        @NamedQuery(name = "GC.findAnalyisByContainerIdForNotRectification", query = "select o from GC o where o.analysisContainer.id = :id and o.forRectification = false"),
        @NamedQuery(name = "GC.findAnalyisByContainerIdForRectification", query = "select o from GC o where o.analysisContainer.id = :id and o.forRectification = true"),
        @NamedQuery(name = "GC.findAnalyisByProductCode", query = "select o from GC o where o.product.code = :code")
})
@XmlRootElement(name = "gc")
@XmlAccessorType(XmlAccessType.FIELD)
public class GC extends Analysis implements Serializable {

    @Lob
    private String gcInfo;

    @Transient
    public boolean gcInfoWired() {
        return (gcInfo != null && !gcInfo.isEmpty());
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programGC", foreignKey = @ForeignKey(name = "fk_programGCForGC"))
    private ProgramGC program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_methodGC", foreignKey = @ForeignKey(name = "fk_methodGCForGC"))
    private MethodGC method;

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    private AnalysisGCTypeEnum analysisGCType;

    @Column(length = 25)
    @Enumerated(value = EnumType.STRING)
    private GivaudanLocationEnum location;

    private boolean forRectification;
//    private boolean sensitive;

    @Transient
    public String cssUtil() {
        switch (getAnalysisGCType()) {
            case CRUDE:
                return "box-border-orange";
            case MONITORING:
                return "box-border-red";
            case RECTIFIED:
                return "box-border-green";
            default:
                return "";
        }
    }

    public String getGcInfo() {
        return gcInfo;
    }

    public void setGcInfo(String gcInfo) {
        this.gcInfo = gcInfo;
    }

    public ProgramGC getProgram() {
        return program;
    }

    public void setProgram(ProgramGC program) {
        this.program = program;
    }

    public MethodGC getMethod() {
        return method;
    }

    public void setMethod(MethodGC method) {
        this.method = method;
    }

    public AnalysisGCTypeEnum getAnalysisGCType() {
        return analysisGCType;
    }

    public void setAnalysisGCType(AnalysisGCTypeEnum analysisGCType) {
        this.analysisGCType = analysisGCType;
    }

    public GivaudanLocationEnum getLocation() {
        return location;
    }

    public void setLocation(GivaudanLocationEnum location) {
        this.location = location;
    }

    public boolean isForRectification() {
        return forRectification;
    }

    public void setForRectification(boolean forRectification) {
        this.forRectification = forRectification;
    }

//    public boolean isSensitive() {
//        return sensitive;
//    }
//
//    public void setSensitive(boolean sensitive) {
//        this.sensitive = sensitive;
//    }
}
