package com.server.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtil {

	public static <T> void exportExcel(String title,String[] headers,String[] columnName,
			HttpServletResponse res,List<T> data,String pattern) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException{
		res.setContentType("application/x-download");
		res.addHeader("Content-Disposition",
				"attachment;filename=" + new String((title).getBytes("GB2312"), "iso8859-1") + ".xls");
		OutputStream out=null;
		try {
			out = res.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(title);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment((short)2);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setFontHeightInPoints((short)10);
		style.setFont(font);
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(columnName[i]);
		}
		for (int i = 0; i < data.size(); i++) {
			HSSFRow rows = sheet.createRow(i+1);
			for (int j = 0; j < headers.length; j++) {
				HSSFCell cell = rows.createCell(j);
				cell.setCellStyle(style);
				Field field = data.get(i).getClass().getDeclaredField(headers[j]);
				field.setAccessible(true);
				if(field.get(data.get(i))!=null){
					cell.setCellValue(field.get(data.get(i)).toString());
				}else{
					cell.setCellValue("null");
				}
			}
		}
		for (int i = 0; i < columnName.length; i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			workbook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ;
	}
}
