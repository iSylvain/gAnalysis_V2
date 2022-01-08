package com.givaudan.galaxy.service;

import com.givaudan.galaxy.dao.AnalysisServiceDAO;
import com.givaudan.galaxy.model.AnalysisReferenceData;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.analysis.GC;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/analysis")
public class AnalysisService {

    private static final Logger logger = Logger.getLogger(AnalysisService.class.getName());

    @Inject
    private AnalysisServiceDAO analysisServiceDAO;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Analysis getAnalysisByID(@PathParam("id") Long id) {
        Analysis analysis = analysisServiceDAO.findAnalysisById(id);
        if (analysis != null) {
            return analysis;
        } else {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
    }

    @GET
    @Path("/byNumber/{number}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Analysis getAnalysisByNumber(@PathParam("number") String number) {
        logger.log(Level.INFO, "AnalysisService.getAnalysisByNumber called with number: {0}", number);

        Analysis analysis = analysisServiceDAO.findAnalysisByNumber(number);
        if (analysis != null) {
            return analysis;
        } else {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
    }

    @GET
    @Path("/gc/byNumber/{number}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public GC getGCByNumber(@PathParam("number") String number) {
        logger.log(Level.INFO, "AnalysisService.getGCByNumber called with number: {0}", number);

        GC analysis = analysisServiceDAO.findGCByNumber(number);
        if (analysis != null) {
            return analysis;
        } else {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
    }

    @GET
    @Path("/chromeleon/byNumber/{number}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ChromeleonService getChromelonByNumber(@PathParam("number") String number) {
        logger.log(Level.INFO, "AnalysisService.getChromelonByNumber called with number: {0}", number);

        GC analysis = analysisServiceDAO.findGCByNumber(number);
        if (analysis != null) {
            ChromeleonService chromeleon = new ChromeleonService();
            chromeleon.setId(analysis.getId());
            chromeleon.setNumber(analysis.getNumber());
            chromeleon.setTypeGC(analysis.getAnalysisGCType().getLabel());
            chromeleon.setSummary(analysis.getSummary());
            chromeleon.setProgramName(analysis.getProgram().getName());
            chromeleon.setMethodName(analysis.getMethod().getName());
            chromeleon.setTypeMethod(analysis.getMethod().getMethodGCType().getLabel());
            chromeleon.setPolarity(analysis.getProgram().getColumn().getPolarity().getLabel());
            if (analysis.getProduct().name_frWired()) {
                chromeleon.setProductName(analysis.getProduct().getName_fr());
                chromeleon.setItemName(analysis.getProduct().getName_fr());
            } else {
                chromeleon.setProductName(analysis.getProduct().getName_en());
                chromeleon.setItemName(analysis.getProduct().getName_en());
            }
            chromeleon.setProductCode(analysis.getProduct().getCode());
            return chromeleon;
        } else {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
    }

    @GET
    @Path("/standards/byNumber/{number}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AnalysisStandardObjectService getAnalysisStandardByNumber(@PathParam("number") String number) {
        logger.log(Level.INFO, "AnalysisService.getAnalysisStandardByNumber called with number: {0}", number);

        GC analysis = analysisServiceDAO.findGCByNumber(number);
        if (analysis != null) {
            if (analysis.isStandardsWired()) {
                AnalysisStandardObjectService analysisStandardObjectService = new AnalysisStandardObjectService();
                analysisStandardObjectService.getStandards().addAll(analysis.getStandards());
                return analysisStandardObjectService;
            } else {
                throw new WebApplicationException(Response.Status.NO_CONTENT);
            }
        } else {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
    }

    @GET
    @Path("/reference/byNumber/{number}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AnalysisDataObjectService getReferenceByNumber(@PathParam("number") String number) {
        logger.log(Level.INFO, "AnalysisService.getReferenceByNumber called with number: {0}", number);

//        throw new WebApplicationException(Response.Status.NO_CONTENT); //PROVISOIRE - bug dans plugin Chromeleon 
        AnalysisReferenceData data = analysisServiceDAO.findReferenceByNumber(number);
        if (data != null) {
            AnalysisDataObjectService result = new AnalysisDataObjectService();
            result.setId(data.getId());
            result.setContent(data.getContent());
            result.setName(data.getName());
            result.setDataSize(data.getDataSize());
            result.setMime(data.getMime());
            result.setModifiedOn(data.getModifiedOn());
            return result;
        } else {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
    }

    @GET
    @Path("/analysisCheckEnd/{productCode}/{operation}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AnalysisCheckEndObjectService getAnalysisCheckEndByProductCodeAndOperation(@PathParam("productCode") String productCode, @PathParam("operation") String operation) {
        logger.log(Level.INFO, "AnalysisService.getAnalysisCheckEndByProductCodeAndOperation called with productCode: {0}", productCode);

        AnalysisCheckEndObjectService result = analysisServiceDAO.findAnalysisCheckEndByProductCodeAndOperation(productCode, operation);
        if (result != null) {
            return result;
        } else {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
    }
}
