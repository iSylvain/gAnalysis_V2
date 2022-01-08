package com.givaudan.galaxy.util;

import com.givaudan.galaxy.model.core.Data;
import com.givaudan.galaxy.model.core.CoreData;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@WebServlet(name = "CoreDataServlet", urlPatterns = "/core/picture/*")
public class CoreDataServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CoreDataServlet.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("CoreDataServlet.doGet called");
        
        String[] ids = request.getParameterValues("id");
        if (ids != null && ids.length == 1) {
            CoreData data = entityManager.find(CoreData.class, Long.parseLong(ids[0]));
            if (data != null) {
                StreamedContent streamedContent = streamedImageByData(data);

                if (streamedContent == null || streamedContent.getStream() == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                    return;
                }

                logger.log(Level.INFO, "data name : {0}", data.getName());
                // Init servlet response.
                response.reset();
                response.setContentType(data.getMime());
                response.setContentLength((int) data.getDataSize());

                // Write image content to response.
                response.getOutputStream().write(data.getContent());
            }
        }
    }

    private StreamedContent streamedImageByData(Data data) {
        return new DefaultStreamedContent(new ByteArrayInputStream(data.getContent()), data.getMime(), data.getName());
    }
    
}
