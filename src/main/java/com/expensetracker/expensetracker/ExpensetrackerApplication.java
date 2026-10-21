package com.expensetracker.expensetracker;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import com.expensetracker.expensetracker.models.Order;

import com.expensetracker.expensetracker.services.CustomerreportService;
import com.expensetracker.expensetracker.services.JReportorderService;
import com.expensetracker.expensetracker.services.JReportService;
import com.expensetracker.expensetracker.services.ReportService;
import com.expensetracker.expensetracker.services.ReportexcelService;
import com.expensetracker.expensetracker.services.ReportassetService;

import com.expensetracker.expensetracker.services.JreportassetService;
import com.expensetracker.expensetracker.services.OrderRepository;
import com.expensetracker.expensetracker.services.OrderreportService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

@SpringBootApplication
@RestController
public class ExpensetrackerApplication {
	@Autowired
    private OrderreportService orderreportService;
	
	@Autowired
    private JReportorderService jreportorderService;
	
	@Autowired
    private ReportService repoService;
	
	@Autowired
    private ReportexcelService repoexcelService;
	
	@Autowired
    private ReportassetService assetrepoService;
	
	
	@Autowired
    private JreportassetService jreportassetService;
	
	
	@Autowired
    private JReportService jreportService;
	
	@Autowired
    private CustomerreportService customerreportService;
	
	@Autowired
    private OrderRepository orderrepository;

	 @GetMapping("/getOrders")
	    public List<Order> getOrders() {

	        return orderrepository.findAll();
	    }
	
	 @GetMapping("/report/{format}")
	    public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException {
	        return orderreportService.exportReport(format);
	    }
	public static void main(String[] args) {
		SpringApplication.run(ExpensetrackerApplication.class, args);
	}
	@GetMapping("/rep/{format}")
    public String generatecustomerReport(@PathVariable String format) throws FileNotFoundException, JRException {
        return customerreportService.exportReport(format);
    }
	 @GetMapping("/jasperpdf/export")
	    public void createcustomerPDF(HttpServletResponse response) throws IOException, JRException {
	       response.setContentType("application/pdf");
	       DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
	       String currentDateTime = dateFormatter.format(new Date());
	  
	       String headerKey = "Content-Disposition";
	       String headerValue = "attachment; filename=customers_" + currentDateTime + ".pdf";
	       response.setHeader(headerKey, headerValue);
	  
	       jreportService.exportJasperReport(response);
	    }
	 @GetMapping("/jasperpdf/order/export")
	    public void createOrderPDF(HttpServletResponse response) throws IOException, JRException {
	       response.setContentType("application/pdf");
	       DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
	       String currentDateTime = dateFormatter.format(new Date());
	  
	       String headerKey = "Content-Disposition";
	       String headerValue = "attachment; filename=Orders_" + currentDateTime + ".pdf";
	       response.setHeader(headerKey, headerValue);
	  
	       jreportorderService.exportJasperReport(response);
	    }
	 @GetMapping("/jasperpdf/asset/export")
	    public void createAssetPDF(HttpServletResponse response) throws IOException, JRException {
	       response.setContentType("application/pdf");
	       DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
	       String currentDateTime = dateFormatter.format(new Date());
	  
	       String headerKey = "Content-Disposition";
	       String headerValue = "attachment; filename=Assets_" + currentDateTime + ".pdf";
	       response.setHeader(headerKey, headerValue);
	  
	       jreportassetService.exportJasperReport(response);
	    }
	
	
	 @GetMapping("/excel")
		public void generateExcelReport(HttpServletResponse response) throws Exception{
			
			response.setContentType("application/octet-stream");
			
			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=order.xls";

			response.setHeader(headerKey, headerValue);
			
			repoService.generateExcel(response);
			
			response.flushBuffer();
		}
	 @GetMapping("/customerexcel")
		public void generateCustomerExcelReport(HttpServletResponse response) throws Exception{
			
			response.setContentType("application/octet-stream");
			
			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=Customer.xls";

			response.setHeader(headerKey, headerValue);
			
			repoexcelService.generateExcel(response);
			
			response.flushBuffer();
		}
	 @GetMapping("/assetexcel")
		public void generateassetExcelReport(HttpServletResponse response) throws Exception{
			
			response.setContentType("application/octet-stream");
			
			String headerKey = "Content-Disposition";
			String headerValue = "attachment;filename=Asset.xls";

			response.setHeader(headerKey, headerValue);
			
			assetrepoService.generateExcel(response);
			
			response.flushBuffer();
		}
}
