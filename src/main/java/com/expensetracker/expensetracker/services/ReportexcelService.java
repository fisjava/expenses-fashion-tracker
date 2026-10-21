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
public class ReportexcelService {

	@Autowired
	private CustomerRepository customererRepo;

	public void generateExcel(HttpServletResponse response) throws Exception {

		List<com.expensetracker.expensetracker.models.Customer> customers = customererRepo.findAll();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Customers Info");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Name");
		row.createCell(2).setCellValue("Phone");
		row.createCell(3).setCellValue("Date");
		row.createCell(4).setCellValue("Location");
		



		int dataRowIndex = 1;

		for (com.expensetracker.expensetracker.models.Customer customer : customers) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(customer.getId());
			dataRow.createCell(1).setCellValue(customer.getName());
			dataRow.createCell(2).setCellValue(customer.getPhone());
			dataRow.createCell(3).setCellValue(customer.getDate());
			dataRow.createCell(4).setCellValue(customer.getLocation().getName());
			
			dataRowIndex++;
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();

	}

}
