package com.givaudan.galaxy.service;

import com.givaudan.galaxy.model.SynthesisData;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

//@Stateless
public class SynthesisClientService {

    private static final Logger logger = Logger.getLogger(SynthesisClientService.class.getName());

//    @PersistenceContext
//    private EntityManager entityManager;

    public SynthesisData getSynthesisData(String procedureCode, String operation) {
        logger.log(Level.INFO, "getSynthesisData called");
        
        //les parameters ne peuvent pas être nuls
        if (procedureCode == null || operation == null) {
            return null;
        }

        SynthesisData result = null;

        logger.log(Level.INFO, "parameters - procedureCode: {0} operation: {1}", new Object[]{procedureCode, operation});

        try {

//            String gSynthesisServiceURL = (String) entityManager.createNativeQuery("SELECT o.value FROM galaxy_settings o where o.key = 'gSynthesis'").getSingleResult();
//            logger.log(Level.INFO, "gSynthesisServiceURL: {0}", gSynthesisServiceURL);
            
            String gSynthesisServiceURL = "http://gvecsl0001gxyp:8080/gSynthesis/";
            
            logger.log(Level.INFO, "gSynthesisServiceURL: {0}rest/product/synthesisData/{1}/{2}", new Object[]{gSynthesisServiceURL, procedureCode, operation});
            
            Client client = ClientBuilder.newClient();

            WebTarget target = client.target(gSynthesisServiceURL + "rest/product/synthesisData/{procedureCode}/{operation}")
                    .resolveTemplate("procedureCode", procedureCode)
                    .resolveTemplate("operation", operation)
                    .queryParam("verbose", true);

            Response response = target.request()
                    .accept("application/xml")
                    .get();

            //200
            if (response.getStatus() == 200) {
                result = response.readEntity(SynthesisData.class);
                logger.log(Level.INFO, "SynthesisData name: {0}", result.getName());
            }

            client.close();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception : {0}", e.getMessage());
        }

        return result;
    }

}
