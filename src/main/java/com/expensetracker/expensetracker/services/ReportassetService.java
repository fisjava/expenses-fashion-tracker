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
public class ReportassetService {

	@Autowired
	private AssetRepository assetRepo;

	public void generateExcel(HttpServletResponse response) throws Exception {

		List<com.expensetracker.expensetracker.models.Asset> assets = assetRepo.findAll();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Assets Info");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Name");
		row.createCell(2).setCellValue("Unit");
		row.createCell(3).setCellValue("Qty");
		row.createCell(4).setCellValue("Price");
		row.createCell(5).setCellValue("Date");
		



		int dataRowIndex = 1;

		for (com.expensetracker.expensetracker.models.Asset asset : assets) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(asset.getId());
			dataRow.createCell(1).setCellValue(asset.getName());
			dataRow.createCell(2).setCellValue(asset.getUnit());
			dataRow.createCell(3).setCellValue(asset.getQtyy());
			dataRow.createCell(4).setCellValue(asset.getPrce());
			dataRow.createCell(5).setCellValue(asset.getDate().toString());
			
			dataRowIndex++;
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();

	}

}
