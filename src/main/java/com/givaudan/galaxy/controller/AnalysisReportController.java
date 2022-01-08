package com.givaudan.galaxy.controller;

import com.givaudan.galaxy.dao.EntityManagerDao;
import com.givaudan.galaxy.exception.NoEntityException;
import com.givaudan.galaxy.model.AnalysisStandard;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.util.AnalysisReportUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

@Named
@ViewScoped
public class AnalysisReportController implements Serializable {

    private static final Logger logger = Logger.getLogger(AnalysisReportController.class.getName());

    @Inject
    private EntityManagerDao entityManagerDAO;

    private Analysis instance;

    @PreDestroy
    public void cleanObject() {
        instance = null;
    }
    
    public void load(Long id) throws ServletException, IOException {

        logger.log(Level.INFO, "load called with ID: {0}", id);

        try {
            CriteriaBuilder cb = entityManagerDAO.getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Analysis> query = cb.createQuery(Analysis.class);
            Root<Analysis> object = query.from(Analysis.class);
            Fetch<Analysis, AnalysisStandard> standards = object.fetch("standards", JoinType.LEFT);
            query.where(cb.equal(object.get("id"), id));
            instance = entityManagerDAO.getEntityManager().createQuery(query).getSingleResult();
        } catch (NoResultException | NonUniqueResultException nre) {
            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            throw new NoEntityException("No entity with id : " + id);
        }

        try {

            String fileName = instance.getProduct().getName_en();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) context.getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Cache-control", "must-revalidate, post-check=0, pre-check=0");
            //download the resource
//            response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");
            response.setHeader("Content-disposition", "inline;filename=\"" + fileName + "\"");

            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

            //Footer
            HeaderFooter event = new HeaderFooter();
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setPageEvent(event);

            document.open();

            //document
            document.addTitle(fileName);
            document.addSubject("gAnalysis");
            document.addAuthor("Tercier Sylvain");
            document.addCreator("Galaxy");

            //create
            AnalysisReportUtil reportUtil = new AnalysisReportUtil();
            reportUtil.createAnaylsisPDF(writer, document, instance, context);
            
            document.close();
            FacesContext.getCurrentInstance().responseComplete();

        } catch (DocumentException de) {
            throw new IOException(de.getMessage());
        }
    }

    /**
     * Inner class to add a header and a footer.
     */
    static class HeaderFooter extends PdfPageEventHelper {

        Phrase[] header = new Phrase[2];
        int pagenumber;

        private static final Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(34, 50, 78));

//        @Override
//        public void onOpenDocument(PdfWriter writer, Document document) {
//            header[0] = new Phrase("Galaxy");
//        }
//        @Override
//        public void onChapter(PdfWriter writer, Document document,
//            float paragraphPosition, Paragraph title) {
//            header[1] = new Phrase(title.getContent());
//            pagenumber = 1;
//        }
        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            Rectangle rect = writer.getBoxSize("art");

            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Galaxy - gAnalysis", font), rect.getLeft(), rect.getBottom() - 25, 0);

            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("page %d", pagenumber), font), (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 25, 0);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
//            System.out.println(dateFormat.format(date)); //2014/08/06

            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(dateFormat.format(date), font), rect.getRight(), rect.getBottom() - 25, 0);

        }
    }

}
