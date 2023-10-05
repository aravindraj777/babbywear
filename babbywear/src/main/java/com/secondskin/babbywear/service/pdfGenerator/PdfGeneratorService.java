package com.secondskin.babbywear.service.pdfGenerator;

import com.secondskin.babbywear.model.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PdfGeneratorService {

    void generatePdf(List<Order>orders, HttpServletResponse response);
}
