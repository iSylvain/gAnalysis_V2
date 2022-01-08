package com.givaudan.galaxy.util;

import java.io.Serializable;

public class InformationAnalysisUtil implements Serializable {
    
    private String number;
    private String typeOfAnalysis;
    private String summary;
    private String analysisInfo;
    private String gcInfo;
    private String methodGC;
    private String programGC;

    public boolean analysisInfoWired() {
        return (analysisInfo != null && !analysisInfo.isEmpty());
    }
    
    public boolean gcInfoWired() {
        return (gcInfo != null && !gcInfo.isEmpty());
    }
    
    public boolean methodGCWired() {
        return (methodGC != null && !methodGC.isEmpty());
    }
    
    public boolean programGCWired() {
        return (programGC != null && !programGC.isEmpty());
    }
    
    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the typeOfAnalysis
     */
    public String getTypeOfAnalysis() {
        return typeOfAnalysis;
    }

    /**
     * @param typeOfAnalysis the typeOfAnalysis to set
     */
    public void setTypeOfAnalysis(String typeOfAnalysis) {
        this.typeOfAnalysis = typeOfAnalysis;
    }

    /**
     * @return the analysisInfo
     */
    public String getAnalysisInfo() {
        return analysisInfo;
    }

    /**
     * @param analysisInfo the analysisInfo to set
     */
    public void setAnalysisInfo(String analysisInfo) {
        this.analysisInfo = analysisInfo;
    }

    /**
     * @return the gcInfo
     */
    public String getGcInfo() {
        return gcInfo;
    }

    /**
     * @param gcInfo the gcInfo to set
     */
    public void setGcInfo(String gcInfo) {
        this.gcInfo = gcInfo;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the methodGC
     */
    public String getMethodGC() {
        return methodGC;
    }

    /**
     * @param methodGC the methodGC to set
     */
    public void setMethodGC(String methodGC) {
        this.methodGC = methodGC;
    }

    /**
     * @return the programGC
     */
    public String getProgramGC() {
        return programGC;
    }

    /**
     * @param programGC the programGC to set
     */
    public void setProgramGC(String programGC) {
        this.programGC = programGC;
    }
}
