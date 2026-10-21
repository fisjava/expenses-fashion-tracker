package com.expensetracker.expensetracker.services;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Order;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportService {

	@Autowired
	private OrderRepository orderRepo;

	public void generateExcel(HttpServletResponse response) throws Exception {

		List<com.expensetracker.expensetracker.models.Order> orders = orderRepo.findAll();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Orders Info");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Customer");
		row.createCell(2).setCellValue("Phone");
		row.createCell(3).setCellValue("Description");
		row.createCell(4).setCellValue("Amount");
		row.createCell(5).setCellValue("Order_Type");
		row.createCell(6).setCellValue("Order_Status");
		row.createCell(7).setCellValue("Date");



		int dataRowIndex = 1;

		for (com.expensetracker.expensetracker.models.Order order : orders) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(order.getId());
			dataRow.createCell(1).setCellValue(order.getCustomer().getName());
			dataRow.createCell(2).setCellValue(order.getCustomer().getPhone());
			dataRow.createCell(3).setCellValue(order.getDescription());
			dataRow.createCell(4).setCellValue(order.getAmount());
			dataRow.createCell(5).setCellValue(order.getOrderType().toString());
			dataRow.createCell(6).setCellValue(order.getOrderStatus().toString());
			dataRow.createCell(7).setCellValue(order.getDate().toString());
			
			dataRowIndex++;
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();

	}

}
