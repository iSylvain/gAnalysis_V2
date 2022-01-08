package com.givaudan.galaxy.util;

import com.givaudan.galaxy.model.AnalysisFlow;
import com.givaudan.galaxy.model.AnalysisForFlow;
import com.givaudan.galaxy.model.SynthesisData;
import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.analysis.GC;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class AnalysisReportUtil {

    private static final Logger logger = Logger.getLogger(AnalysisReportUtil.class.getName());

    private static final Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(34, 50, 78));
    private static final Font textGrayFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(138, 143, 156));
    private static final Font textFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(34, 50, 78));

    private PdfPCell getCell() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);   // removes border
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        return cell;
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font, BaseColor backgroundColor, float height, boolean ifBroder) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell color
        cell.setBackgroundColor(backgroundColor);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(50f);
        }
        if (ifBroder) {
            cell.setBorderColor(BaseColor.LIGHT_GRAY);
        } else {
            cell.setBorder(Rectangle.NO_BORDER);   // removes border
        }
        cell.setMinimumHeight(height);
        //add the call to the table
        table.addCell(cell);

    }

    public void createAnaylsisFlowPDF(PdfWriter writer, Document document, AnalysisFlow instance, SynthesisData synthesisData, ExternalContext context) throws IOException {
        logger.log(Level.INFO, "createAnaylsisFlowPDF called");

        try {

            //logo
            try {
                String relativeWebPath = "/resources/img/logo.png";
                ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String url = ctx.getRealPath("/");
//                logger.log(Level.INFO, "logo path: {0}", url + relativeWebPath);
                Image img = Image.getInstance(url + relativeWebPath);
                img.setAbsolutePosition(500f, 750f);
                img.scaleToFit(40, 40);
                document.add(img);
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
            }

//            //QRCode
//            HttpServletRequest request = (HttpServletRequest) context.getRequest();
//            StringBuffer restOfTheUrl = (StringBuffer) request.getRequestURL();
//            document.add(getQRCode(path));

            //Product infos
            Paragraph productInfoParagraph = new Paragraph();

            productInfoParagraph.add(new Phrase(instance.getProduct().getName_msg(), titleFont));

            PdfPTable table = new PdfPTable(2); // 2 columns.
            table.setSpacingAfter(5f);
            table.setWidthPercentage(100);
            float[] columnWidths = {1f, 2f};
            table.setWidths(columnWidths);

            PdfPCell cell1 = getCell();
            cell1.setPhrase(new Phrase(new MessageProvider().getValue("msg", "product_code")));
            PdfPCell cell2 = getCell();
            cell2.setPhrase(new Phrase(instance.getProduct().getCode()));

            table.addCell(cell1);
            table.addCell(cell2);

            productInfoParagraph.add(table);
            productInfoParagraph.setSpacingBefore(60);
            document.add(productInfoParagraph);

            //Analysis flow
            Paragraph moduleInfoParagraph = new Paragraph();

            PdfPTable moduleInfoTable = new PdfPTable(2); // 2 columns.
            moduleInfoTable.setSpacingBefore(10f);
            moduleInfoTable.setSpacingAfter(10f);
            moduleInfoTable.setWidthPercentage(100);
            moduleInfoTable.setWidths(columnWidths);

            PdfPCell moduleInfoCell1 = getCell();
            PdfPCell moduleInfoCell2 = getCell();

            moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "procedureCode"), textGrayFont));
            moduleInfoTable.addCell(moduleInfoCell1);
            moduleInfoCell2.setPhrase(new Phrase(instance.getProcessCode(), textFont));
            moduleInfoTable.addCell(moduleInfoCell2);

            moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "procedureVersion"), textGrayFont));
            moduleInfoTable.addCell(moduleInfoCell1);
            moduleInfoCell2.setPhrase(new Phrase(instance.getProcessVersion() + "", textFont));
            moduleInfoTable.addCell(moduleInfoCell2);

            if (instance.locationDetailWired()) {

                moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("msg", "shed"), textGrayFont));
                moduleInfoTable.addCell(moduleInfoCell1);
                moduleInfoCell2.setPhrase(new Phrase(instance.getLocationDetail(), textFont));
                moduleInfoTable.addCell(moduleInfoCell2);

            }

            if (instance.informationWired()) {

                moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("msg", "info"), textGrayFont));
                moduleInfoTable.addCell(moduleInfoCell1);
                moduleInfoCell2 = getCell();
                if (instance.getInformation().contains("</")) {
                    for (Element e : encodeFromHTML(instance.getInformation())) {
                        moduleInfoCell2.addElement(e);
                    }
                } else {
                    moduleInfoCell2.addElement(new Phrase(instance.getInformation(), textFont));
                }
                moduleInfoTable.addCell(moduleInfoCell2);
            }

            moduleInfoParagraph.add(moduleInfoTable);
            document.add(moduleInfoParagraph);

            Paragraph flowParagraph = new Paragraph();

            PdfPTable dataTable = new PdfPTable(4); // 4 columns.
            dataTable.setSpacingBefore(20f);
            dataTable.setSpacingAfter(10f);
            dataTable.setWidthPercentage(100);

            //specify column widths
            float[] columnWidthsTable = {2f, 1f, 4f, 2f};
            // set table width a percentage of the page width
            dataTable.setWidths(columnWidthsTable);

            for (AnalysisForFlow a : instance.getAnalysisForFlowList()) {

                insertCell(dataTable, new MessageProvider().getValue("module", "processStepNumberLight") + " " + a.getProcessStepNumber(), Element.ALIGN_LEFT, 1, textGrayFont, BaseColor.WHITE, 50f, false);
                insertCell(dataTable, new MessageProvider().getValue("module", a.getAnalysis().getTypeOfAnalysis()), Element.ALIGN_LEFT, 1, textGrayFont, BaseColor.WHITE, 50f, false);
                insertCell(dataTable, a.getAnalysis().getSummary(), Element.ALIGN_LEFT, 1, textFont, BaseColor.WHITE, 50f, false);

                if (a.getAnalysis().getTypeOfAnalysis().equals("gc")) {
                    //code128
                    PdfPCell cell = new PdfPCell(getCode128(a.getAnalysis().getNumber(), writer));
                    //set the cell alignment
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setBorder(Rectangle.NO_BORDER);   // removes border
                    dataTable.addCell(cell);
                } else {
                    PdfPCell cell = new PdfPCell(new Phrase(""));
                    cell.setBorder(Rectangle.NO_BORDER);   // removes border
                    dataTable.addCell(cell);
                }
            }

            flowParagraph.add(dataTable);
            document.add(flowParagraph);

            //gSynthesis
            if (synthesisData != null) {
                document.newPage();

                Paragraph synthesisParagraph = new Paragraph();

                PdfPTable synthesisTable = new PdfPTable(1); // 1 column.
                synthesisTable.setSpacingBefore(10f);
                synthesisTable.setSpacingAfter(10f);
                synthesisTable.setWidthPercentage(100);
                float[] columnWidthsSynthesisTable = {1f};
                synthesisTable.setWidths(columnWidthsSynthesisTable);

                PdfPCell synthesisCell = getCell();
                synthesisCell.setPhrase(new Phrase(new MessageProvider().getValue("module", "synthesis"), textGrayFont));
                synthesisTable.addCell(synthesisCell);

//                PdfContentByte cb = writer.getDirectContent();
                Image synthesisImage = Image.getInstance(synthesisData.getContent());
//                synthesisImage.scaleToFit(400, 400);
//                synthesisImage.setAlignment(Element.ALIGN_CENTER);
//                synthesisImage.setAbsolutePosition(0f, 0f);
//                cb.addImage(synthesisImage);
                synthesisCell = new PdfPCell(synthesisImage, true);
                synthesisCell.setBorder(Rectangle.NO_BORDER);   // removes border
                synthesisCell.setPaddingTop(50);
                synthesisTable.addCell(synthesisCell);

                synthesisParagraph.add(synthesisTable);
                document.add(synthesisParagraph);
            }

        } catch (DocumentException de) {
            throw new IOException(de.getMessage());
        }
    }

    public void createAnaylsisPDF(PdfWriter writer, Document document, Analysis instance, ExternalContext context) throws IOException {
        logger.log(Level.INFO, "createAnaylsisPDF called");
        logger.log(Level.INFO, "N° analysis: " + instance.getNumber());

        try {

            //logo
            try {
                String relativeWebPath = "/resources/img/logo.png";
                ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String url = ctx.getRealPath("/");
//                logger.log(Level.INFO, "logo path: {0}", url + relativeWebPath);
                Image img = Image.getInstance(url + relativeWebPath);
                img.setAbsolutePosition(500f, 750f);
                img.scaleToFit(40, 40);
                document.add(img);
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
            }

//            //QRCode
//            HttpServletRequest request = (HttpServletRequest) context.getRequest();
//            StringBuffer restOfTheUrl = (StringBuffer) request.getRequestURL();
//            String path = restOfTheUrl + "?id=" + instance.getId();
//            document.add(getQRCode(path));

            //code128
            Paragraph code128Paragraph = new Paragraph();
            code128Paragraph.add(getCode128(instance.getNumber(), writer));
            code128Paragraph.setSpacingBefore(20f);
            document.add(code128Paragraph);

            //Product infos
            document.add(new Paragraph(instance.getProduct().getName_msg(), titleFont));

            Paragraph productInfoParagraph = new Paragraph();

            PdfPTable table = new PdfPTable(2); // 2 columns.
            table.setSpacingBefore(5f);
            table.setSpacingAfter(5f);
            table.setWidthPercentage(100);
            float[] columnWidths = {1f, 2f};
            table.setWidths(columnWidths);

            PdfPCell cell1 = getCell();
            cell1.setPhrase(new Phrase(new MessageProvider().getValue("msg", "product_code")));
            PdfPCell cell2 = getCell();
            cell2.setPhrase(new Phrase(instance.getProduct().getCode()));

            table.addCell(cell1);
            table.addCell(cell2);

            productInfoParagraph.add(table);
            document.add(productInfoParagraph);

            //Analysis
            Paragraph summaryParagraph = new Paragraph();

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setSpacingBefore(10f);
            summaryTable.setSpacingAfter(10f);
            summaryTable.setWidthPercentage(100);
            summaryTable.setWidths(columnWidths);

            BaseColor baseColorResume = new BaseColor(176, 196, 222);

            PdfPCell summaryCell1 = getCell();
            summaryCell1.setPaddingTop(15);
            summaryCell1.setPaddingBottom(15);
            summaryCell1.setBackgroundColor(baseColorResume);
            summaryCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "summary"), textFont));
            summaryTable.addCell(summaryCell1);

            PdfPCell summaryCell2 = getCell();
            summaryCell2.setPaddingTop(15);
            summaryCell2.setPaddingBottom(15);
            summaryCell2.setBackgroundColor(baseColorResume);
            summaryCell2.setPhrase(new Phrase(instance.getSummary(), textFont));
            summaryTable.addCell(summaryCell2);

            summaryParagraph.add(summaryTable);
            document.add(summaryParagraph);

            Paragraph moduleInfoParagraph = new Paragraph();

            PdfPTable moduleInfoTable = new PdfPTable(2); // 2 columns.
            moduleInfoTable.setWidthPercentage(100);
            moduleInfoTable.setWidths(columnWidths);

            PdfPCell moduleInfoCell1 = getCell();
            PdfPCell moduleInfoCell2 = getCell();

            moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "typeOfAnalysis"), textGrayFont));
            moduleInfoTable.addCell(moduleInfoCell1);
            moduleInfoCell2.setPhrase(new Phrase(new MessageProvider().getValue("module", instance.getTypeOfAnalysis()), textFont));
            moduleInfoTable.addCell(moduleInfoCell2);

            moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "analysis_number"), textGrayFont));
            moduleInfoTable.addCell(moduleInfoCell1);
            moduleInfoCell2.setPhrase(new Phrase(instance.getNumber(), textFont));
            moduleInfoTable.addCell(moduleInfoCell2);

            if (instance.analysisInfoWired()) {
                moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("msg", "info"), textGrayFont));
                moduleInfoTable.addCell(moduleInfoCell1);
                if (instance.getAnalysisInfo().contains("<") && instance.getAnalysisInfo().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getAnalysisInfo())) {
                        moduleInfoCell2.addElement(e);
                    }
                } else {
                    moduleInfoCell2.addElement(new Phrase(instance.getAnalysisInfo(), textFont));
                }
                moduleInfoTable.addCell(moduleInfoCell2);
            }

            if (instance.appearanceSampleWired()) {

                moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "appearance"), textGrayFont));
                moduleInfoTable.addCell(moduleInfoCell1);
                moduleInfoCell2 = getCell();
                if (instance.getAppearanceSample().contains("<") && instance.getAppearanceSample().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getAppearanceSample())) {
                        moduleInfoCell2.addElement(e);
                    }
                } else {
                    moduleInfoCell2.addElement(new Phrase(instance.getAppearanceSample(), textFont));
                }
                moduleInfoTable.addCell(moduleInfoCell2);

            }

            moduleInfoParagraph.add(moduleInfoTable);
            document.add(moduleInfoParagraph);

            if (instance.preparationWired()) {

                Paragraph analysisInfoParagraph = new Paragraph();

                PdfPTable analysisInfoTable = new PdfPTable(1); // 1 column.
                analysisInfoTable.setSpacingBefore(10f);
                analysisInfoTable.setSpacingAfter(10f);
                analysisInfoTable.setWidthPercentage(100);
                float[] columnWidthsPreparation = {1f};
                analysisInfoTable.setWidths(columnWidthsPreparation);

                PdfPCell analysisInfoCell1 = getCell();
                BaseColor baseColorPreparation = new BaseColor(253, 245, 230);

                analysisInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "preparation"), textGrayFont));
                analysisInfoCell1.setBackgroundColor(baseColorPreparation);
                analysisInfoTable.addCell(analysisInfoCell1);

                analysisInfoCell1 = getCell();
                if (instance.getPreparation().contains("<") && instance.getPreparation().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getPreparation())) {
                        analysisInfoCell1.addElement(e);
                    }
                } else {
                    analysisInfoCell1.addElement(new Phrase(instance.getPreparation(), textFont));
                }
                analysisInfoCell1.setBackgroundColor(baseColorPreparation);
                analysisInfoTable.addCell(analysisInfoCell1);

                analysisInfoParagraph.add(analysisInfoTable);
                document.add(analysisInfoParagraph);
            }

            if (instance.calculationWired()) {

                Paragraph analysisInfoParagraph = new Paragraph();

                PdfPTable analysisInfoTable = new PdfPTable(1); // 1 column.
                analysisInfoTable.setSpacingBefore(10f);
                analysisInfoTable.setSpacingAfter(10f);
                analysisInfoTable.setWidthPercentage(100);
                float[] columnWidthsPreparation = {1f};
                analysisInfoTable.setWidths(columnWidthsPreparation);

                PdfPCell analysisInfoCell1 = getCell();
                BaseColor baseColorPreparation = new BaseColor(253, 245, 230);

                analysisInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "calculation"), textGrayFont));
                analysisInfoCell1.setBackgroundColor(baseColorPreparation);
                analysisInfoTable.addCell(analysisInfoCell1);

                analysisInfoCell1 = getCell();
                if (instance.getCalculation().contains("<") && instance.getCalculation().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getCalculation())) {
                        analysisInfoCell1.addElement(e);
                    }
                } else {
                    analysisInfoCell1.addElement(new Phrase(instance.getCalculation(), textFont));
                }
                analysisInfoCell1.setBackgroundColor(baseColorPreparation);
                analysisInfoTable.addCell(analysisInfoCell1);

                analysisInfoParagraph.add(analysisInfoTable);
                document.add(analysisInfoParagraph);
            }

            if (instance.isStandardsWired()) {

                Paragraph paragraph = new Paragraph();

                paragraph.add(new Phrase(new MessageProvider().getValue("module", "analysisStandards"), textGrayFont));

                PdfPTable dataTable = new PdfPTable(4); // 4 columns.
                dataTable.setSpacingBefore(10f);
                dataTable.setSpacingAfter(10f);
                dataTable.setWidthPercentage(100);

                //specify column widths
                float[] columnWidthsTable = {3f, 2f, 1f, 1f};
                // set table width a percentage of the page width
                dataTable.setWidths(columnWidthsTable);

                //insert column headings
                insertCell(dataTable, "Name", Element.ALIGN_CENTER, 1, textFont, BaseColor.LIGHT_GRAY, 20f, true);
                insertCell(dataTable, "ID", Element.ALIGN_CENTER, 1, textFont, BaseColor.LIGHT_GRAY, 20f, true);
                insertCell(dataTable, "Min.", Element.ALIGN_CENTER, 1, textFont, BaseColor.LIGHT_GRAY, 20f, true);
                insertCell(dataTable, "Max.", Element.ALIGN_CENTER, 1, textFont, BaseColor.LIGHT_GRAY, 20f, true);
                dataTable.setHeaderRows(1);

                instance.getStandards().stream().map((a) -> {
//                    logger.log(Level.INFO, "standard name : {0}", a.getName());
                    return a;
                }).map((a) -> {
                    insertCell(dataTable, a.getName(), Element.ALIGN_CENTER, 1, textGrayFont, BaseColor.WHITE, 20f, true);
                    return a;
                }).forEach((a) -> {
                    String value = "-";
                    if (a.isAnalysisStandardIDWired()) {
                        value = a.getAnalysisStandardID();
                    }
                    insertCell(dataTable, value, Element.ALIGN_CENTER, 1, textGrayFont, BaseColor.WHITE, 20f, true);

                    if (a.isUnitWired()) {
                        value = a.getUnit();
                    } else {
                        value = "";
                    }
                    insertCell(dataTable, a.getMinimumStandard() + value, Element.ALIGN_CENTER, 1, textGrayFont, BaseColor.WHITE, 20f, true);
                    insertCell(dataTable, a.getMaximumStandard() + value, Element.ALIGN_CENTER, 1, textGrayFont, BaseColor.WHITE, 20f, true);
                });

                paragraph.add(dataTable);
                document.add(paragraph);
            }

        } catch (DocumentException de) {
            throw new IOException(de.getMessage());
        }
    }

    public void createAnaylsisGCPDF(PdfWriter writer, Document document, GC instance, ExternalContext context) throws IOException {
        logger.log(Level.INFO, "createAnaylsisGCPDF called");
        logger.log(Level.INFO, "N° analysis: " + instance.getNumber());

        try {

            String fileName = instance.getProduct().getName_en();

            //logo
            try {
                String relativeWebPath = "/resources/img/logo.png";
                ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String url = ctx.getRealPath("/");
//                logger.log(Level.INFO, "logo path: {0}", url + relativeWebPath);
                Image img = Image.getInstance(url + relativeWebPath);
                img.setAbsolutePosition(500f, 750f);
                img.scaleToFit(40, 40);
                document.add(img);
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
            }

//            //QRCode
//            HttpServletRequest request = (HttpServletRequest) context.getRequest();
//            StringBuffer restOfTheUrl = (StringBuffer) request.getRequestURL();
//            document.add(getQRCode(path));

            //code128
            Paragraph code128Paragraph = new Paragraph();
            code128Paragraph.add(getCode128(instance.getNumber(), writer));
            code128Paragraph.setSpacingBefore(20f);
            document.add(code128Paragraph);

            //Product infos
            document.add(new Paragraph(instance.getProduct().getName_msg(), titleFont));

            Paragraph productInfoParagraph = new Paragraph();

            PdfPTable table = new PdfPTable(2); // 2 columns.
            table.setSpacingBefore(5f);
            table.setSpacingAfter(5f);
            table.setWidthPercentage(100);
            float[] columnWidths = {1f, 2f};
            table.setWidths(columnWidths);

            PdfPCell cell1 = getCell();
            cell1.setPhrase(new Phrase(new MessageProvider().getValue("msg", "product_code")));
            PdfPCell cell2 = getCell();
            cell2.setPhrase(new Phrase(instance.getProduct().getCode()));

            table.addCell(cell1);
            table.addCell(cell2);

            productInfoParagraph.add(table);
            document.add(productInfoParagraph);

            //Analysis
            Paragraph summaryParagraph = new Paragraph();

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setSpacingBefore(10f);
            summaryTable.setSpacingAfter(10f);
            summaryTable.setWidthPercentage(100);
            summaryTable.setWidths(columnWidths);

            BaseColor baseColorResume = new BaseColor(176, 196, 222);

            PdfPCell summaryCell1 = getCell();
            summaryCell1.setPaddingTop(15);
            summaryCell1.setPaddingBottom(15);
            summaryCell1.setBackgroundColor(baseColorResume);
            summaryCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "summary"), textFont));
            summaryTable.addCell(summaryCell1);

            PdfPCell summaryCell2 = getCell();
            summaryCell2.setPaddingTop(15);
            summaryCell2.setPaddingBottom(15);
            summaryCell2.setBackgroundColor(baseColorResume);
            summaryCell2.setPhrase(new Phrase(instance.getSummary(), textFont));
            summaryTable.addCell(summaryCell2);

            summaryParagraph.add(summaryTable);
            document.add(summaryParagraph);

            Paragraph moduleInfoParagraph = new Paragraph();

            PdfPTable moduleInfoTable = new PdfPTable(2); // 2 columns.
            moduleInfoTable.setSpacingBefore(10f);
            moduleInfoTable.setSpacingAfter(10f);
            moduleInfoTable.setWidthPercentage(100);
            moduleInfoTable.setWidths(columnWidths);

            PdfPCell moduleInfoCell1 = getCell();
            PdfPCell moduleInfoCell2 = getCell();

            moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "typeOfAnalysis"), textGrayFont));
            moduleInfoTable.addCell(moduleInfoCell1);
            moduleInfoCell2.setPhrase(new Phrase(new MessageProvider().getValue("module", instance.getTypeOfAnalysis()), textFont));
            moduleInfoTable.addCell(moduleInfoCell2);

            moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "analysis_number"), textGrayFont));
            moduleInfoTable.addCell(moduleInfoCell1);
            moduleInfoCell2.setPhrase(new Phrase(instance.getNumber(), textFont));
            moduleInfoTable.addCell(moduleInfoCell2);

            if (instance.analysisInfoWired()) {

                moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("msg", "info"), textGrayFont));
                moduleInfoTable.addCell(moduleInfoCell1);
                if (instance.getAnalysisInfo().contains("<") && instance.getAnalysisInfo().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getAnalysisInfo())) {
                        moduleInfoCell2.addElement(e);
                    }
                } else {
                    moduleInfoCell2.addElement(new Phrase(instance.getAnalysisInfo(), textFont));
                }
                moduleInfoTable.addCell(moduleInfoCell2);
            }

            moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "programGC"), textGrayFont));
            moduleInfoTable.addCell(moduleInfoCell1);
            moduleInfoCell2.setPhrase(new Phrase(instance.getProgram().getName(), textFont));
            moduleInfoTable.addCell(moduleInfoCell2);

            moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "methodGC"), textGrayFont));
            moduleInfoTable.addCell(moduleInfoCell1);
            moduleInfoCell2.setPhrase(new Phrase(instance.getMethod().getName(), textFont));
            moduleInfoTable.addCell(moduleInfoCell2);

            if (instance.gcInfoWired()) {

                moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "gcInfo"), textGrayFont));
                moduleInfoTable.addCell(moduleInfoCell1);
                moduleInfoCell2 = getCell();
                if (instance.getGcInfo().contains("<") && instance.getGcInfo().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getGcInfo())) {
                        moduleInfoCell2.addElement(e);
                    }
                } else {
                    moduleInfoCell2.addElement(new Phrase(instance.getGcInfo(), textFont));
                }
                moduleInfoTable.addCell(moduleInfoCell2);

            }

            if (instance.appearanceSampleWired()) {

                moduleInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "appearance"), textGrayFont));
                moduleInfoTable.addCell(moduleInfoCell1);
                moduleInfoCell2 = getCell();
                if (instance.getAppearanceSample().contains("<") && instance.getAppearanceSample().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getAppearanceSample())) {
                        moduleInfoCell2.addElement(e);
                    }
                } else {
                    moduleInfoCell2.addElement(new Phrase(instance.getAppearanceSample(), textFont));
                }
                moduleInfoTable.addCell(moduleInfoCell2);

            }

            moduleInfoParagraph.add(moduleInfoTable);
            document.add(moduleInfoParagraph);

            if (instance.preparationWired()) {

                Paragraph analysisInfoParagraph = new Paragraph();

                PdfPTable analysisInfoTable = new PdfPTable(1); // 1 column.
                analysisInfoTable.setSpacingBefore(10f);
                analysisInfoTable.setSpacingAfter(10f);
                analysisInfoTable.setWidthPercentage(100);
                float[] columnWidthsPreparation = {1f};
                analysisInfoTable.setWidths(columnWidthsPreparation);

                PdfPCell analysisInfoCell1 = getCell();
                BaseColor baseColorPreparation = new BaseColor(253, 245, 230);

                analysisInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "preparation"), textGrayFont));
                analysisInfoCell1.setBackgroundColor(baseColorPreparation);
                analysisInfoTable.addCell(analysisInfoCell1);

                analysisInfoCell1 = getCell();
                if (instance.getPreparation().contains("<") && instance.getPreparation().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getPreparation())) {
                        analysisInfoCell1.addElement(e);
                    }
                } else {
                    analysisInfoCell1.addElement(new Phrase(instance.getPreparation(), textFont));
                }
                analysisInfoCell1.setBackgroundColor(baseColorPreparation);
                analysisInfoTable.addCell(analysisInfoCell1);

                analysisInfoParagraph.add(analysisInfoTable);
                document.add(analysisInfoParagraph);
            }

            if (instance.calculationWired()) {

                Paragraph analysisInfoParagraph = new Paragraph();

                PdfPTable analysisInfoTable = new PdfPTable(1); // 1 column.
                analysisInfoTable.setSpacingBefore(10f);
                analysisInfoTable.setSpacingAfter(10f);
                analysisInfoTable.setWidthPercentage(100);
                float[] columnWidthsPreparation = {1f};
                analysisInfoTable.setWidths(columnWidthsPreparation);

                PdfPCell analysisInfoCell1 = getCell();
                BaseColor baseColorPreparation = new BaseColor(253, 245, 230);

                analysisInfoCell1.setPhrase(new Phrase(new MessageProvider().getValue("module", "calculation"), textGrayFont));
                analysisInfoCell1.setBackgroundColor(baseColorPreparation);
                analysisInfoTable.addCell(analysisInfoCell1);

                analysisInfoCell1 = getCell();
                if (instance.getCalculation().contains("<") && instance.getCalculation().contains(">")) {
                    for (Element e : encodeFromHTML(instance.getCalculation())) {
                        analysisInfoCell1.addElement(e);
                    }
                } else {
                    analysisInfoCell1.addElement(new Phrase(instance.getCalculation(), textFont));
                }
                analysisInfoCell1.setBackgroundColor(baseColorPreparation);
                analysisInfoTable.addCell(analysisInfoCell1);

                analysisInfoParagraph.add(analysisInfoTable);
                document.add(analysisInfoParagraph);
            }

            if (instance.isStandardsWired()) {

                Paragraph paragraph = new Paragraph();

                paragraph.add(new Phrase(new MessageProvider().getValue("module", "analysisStandards"), textGrayFont));

                PdfPTable dataTable = new PdfPTable(4); // 4 columns.
                dataTable.setSpacingBefore(10f);
                dataTable.setSpacingAfter(10f);
                dataTable.setWidthPercentage(100);

                //specify column widths
                float[] columnWidthsTable = {3f, 2f, 1f, 1f};
                // set table width a percentage of the page width
                dataTable.setWidths(columnWidthsTable);

                //insert column headings
                insertCell(dataTable, "Name", Element.ALIGN_CENTER, 1, textFont, BaseColor.LIGHT_GRAY, 20f, true);
                insertCell(dataTable, "ID", Element.ALIGN_CENTER, 1, textFont, BaseColor.LIGHT_GRAY, 20f, true);
                insertCell(dataTable, "Min.", Element.ALIGN_CENTER, 1, textFont, BaseColor.LIGHT_GRAY, 20f, true);
                insertCell(dataTable, "Max.", Element.ALIGN_CENTER, 1, textFont, BaseColor.LIGHT_GRAY, 20f, true);
                dataTable.setHeaderRows(1);

                instance.getStandards().stream().map((a) -> {
//                    logger.log(Level.INFO, "standard name : {0}", a.getName());
                    return a;
                }).map((a) -> {
                    insertCell(dataTable, a.getName(), Element.ALIGN_CENTER, 1, textGrayFont, BaseColor.WHITE, 20f, true);
                    return a;
                }).forEach((a) -> {
                    String value = "-";
                    if (a.isAnalysisStandardIDWired()) {
                        value = a.getAnalysisStandardID();
                    }
                    insertCell(dataTable, value, Element.ALIGN_CENTER, 1, textGrayFont, BaseColor.WHITE, 20f, true);

                    if (a.isUnitWired()) {
                        value = a.getUnit();
                    } else {
                        value = "";
                    }
                    insertCell(dataTable, a.getMinimumStandard() + value, Element.ALIGN_CENTER, 1, textGrayFont, BaseColor.WHITE, 20f, true);
                    insertCell(dataTable, a.getMaximumStandard() + value, Element.ALIGN_CENTER, 1, textGrayFont, BaseColor.WHITE, 20f, true);
                });

                paragraph.add(dataTable);
                document.add(paragraph);

            }

            if (instance.isReferenceDataWired()) {
                logger.log(Level.INFO, "instance.isReferenceDataWired()");
                // Load existing PDF
                PdfReader reader = new PdfReader(instance.getReferenceData().getContent());
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    PdfImportedPage page = writer.getImportedPage(reader, i);
                    document.newPage();
                    PdfContentByte cb = writer.getDirectContent();
                    cb.addTemplate(page, 0, 0);
                }
            }

        } catch (DocumentException de) {
            throw new IOException(de.getMessage());
        }
    }

    private ElementList encodeFromHTML(String htmlContent) {
        logger.log(Level.INFO, "encodeFromHTML called.");

        htmlContent = "<div>" + htmlContent + "</div>";
        htmlContent = htmlContent.replaceAll("<br>", "<br/>");
//        logger.log(Level.INFO, "htmlContent : {0}", htmlContent);

        try {
            // CSS
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
            try {
                cssResolver.addCss("span{font-family: Helvetica; font-size: 13px; font-weight: normal; color: #22324E;}", true);
                cssResolver.addCss("div{font-family: Helvetica; font-size: 13px; font-weight: normal; color: #22324E;}", true);
                cssResolver.addCss("ol{font-family: Helvetica; font-size: 13px; font-weight: normal; color: #22324E;}", true);
            } catch (CssResolverException ex) {
                logger.log(Level.SEVERE, "CssResolverException: {0}", ex);
            }
            // HTML
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            htmlContext.autoBookmark(false);
            // Pipelines
            ElementList elements = new ElementList();
            ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
            HtmlPipeline html = new HtmlPipeline(htmlContext, end);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
            // XML Worker
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            p.parse(new StringReader(htmlContent));

            return elements;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException: {0}", ex);
            return null;
        }

    }

    private Image getQRCode(String content) throws BadElementException {
        logger.log(Level.INFO, "QRCode content: {0}", content);
        BarcodeQRCode qrcode = new BarcodeQRCode(content, 100, 100, null);
        Image image = qrcode.getImage();
//        image.setAbsolutePosition(15f,720f);
        return image;
    }

    private Image getCode128(String content, PdfWriter writer) throws BadElementException {
        Barcode128 code128 = new Barcode128();
        code128.setCodeType(Barcode.CODE128_RAW);
        code128.setGenerateChecksum(true);
        code128.setCode(Barcode128.getRawText(content, false));
        code128.setBarHeight(30);
        Image image = code128.createImageWithBarcode(writer.getDirectContent(), null, null);
        return image;
    }

}
