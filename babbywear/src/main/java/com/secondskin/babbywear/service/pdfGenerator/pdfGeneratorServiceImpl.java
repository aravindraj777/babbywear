package com.secondskin.babbywear.service.pdfGenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.secondskin.babbywear.model.Order;
import com.secondskin.babbywear.service.sales.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class pdfGeneratorServiceImpl implements PdfGeneratorService{

    @Autowired
    SalesService salesService;


    @Override
    public void generatePdf(List<Order> orders, HttpServletResponse response) {


        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=salesReport.pdf";
        response.setHeader(headerKey,headerValue);

        try{
            Document document = new Document();
            PdfWriter.getInstance(document,response.getOutputStream());

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12, BaseColor.BLACK);
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA,10,BaseColor.BLACK);

            document.open();

            PdfPTable table = new PdfPTable(8);
            float[] columnWidths = {1.5f,3.5f, 2.5f, 4f, 4f, 3f, 3f,3f};
            table.setWidths(columnWidths);

            PdfPCell cell = new PdfPCell(new Phrase("Sales Report",headerFont));
            cell.setColspan(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            PdfPCell headerCell = new PdfPCell();
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setPadding(5);

            PdfPCell dataCell = new PdfPCell();
            dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            dataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dataCell.setPadding(4);

            headerCell.setPhrase(new Phrase("SN",headerFont));
            table.addCell(headerCell);

            headerCell.setPhrase(new Phrase("Order ID", headerFont));
            table.addCell(headerCell);

            headerCell.setPhrase(new Phrase("User", headerFont));
            table.addCell(headerCell);

            headerCell.setPhrase(new Phrase("Product", headerFont));
            table.addCell(headerCell);

            headerCell.setPhrase(new Phrase("Order Date", headerFont));
            table.addCell(headerCell);

            headerCell.setPhrase(new Phrase("Status", headerFont));
            table.addCell(headerCell);

            headerCell.setPhrase(new Phrase("Price", headerFont));
            table.addCell(headerCell);
            headerCell.setPhrase(new Phrase("Payment", headerFont));
            table.addCell(headerCell);

            int sn = 1;
            for (Order order : orders) {
                String product = order.getOrderItems().stream().map(orderItem -> orderItem.getVariant()
                        .getProducts().getProductName()).collect(Collectors.joining());

                dataCell.setPhrase(new Phrase(String.valueOf(sn), cellFont));
                table.addCell(dataCell);
                sn++;
                dataCell.setPhrase(new Phrase(order.getOrderNumber(), cellFont));
                table.addCell(dataCell);

                dataCell.setPhrase(new Phrase(order.getUserInfo().getUserName(), cellFont));
                table.addCell(dataCell);

                dataCell.setPhrase(new Phrase(product, cellFont));
                table.addCell(dataCell);

                dataCell.setPhrase(new Phrase(order.getOrderedDate().toString(), cellFont));
                table.addCell(dataCell);

                dataCell.setPhrase(new Phrase(order.getStatus().toString(), cellFont));
                table.addCell(dataCell);

                dataCell.setPhrase(new Phrase(String.valueOf(order.getTotal()), cellFont));
                table.addCell(dataCell);

                dataCell.setPhrase(new Phrase(String.valueOf(order.getPayment()), cellFont));
                table.addCell(dataCell);
            }
            PdfPCell summaryCell = new PdfPCell(new Phrase("Summary", headerFont));
            summaryCell.setColspan(2);
            summaryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(summaryCell);

            double totalSales = salesService.calculateTotalSales(orders);
            int totalOrders = orders.size();

            PdfPCell totalOrdersCell = new PdfPCell(new Phrase("Total Orders: " + totalOrders, cellFont));
            totalOrdersCell.setColspan(3);
            totalOrdersCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(totalOrdersCell);

            PdfPCell totalSalesCell = new PdfPCell(new Phrase("Total Sales: " + totalSales, cellFont));
            totalSalesCell.setColspan(3);
            totalSalesCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalSalesCell);
            document.add(table);
            document.close();

        }catch (DocumentException | IOException e){
            e.printStackTrace();
        }

    }
}
