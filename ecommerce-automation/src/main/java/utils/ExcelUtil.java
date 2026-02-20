package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	private static final Logger logger = LogManager.getLogger(ExcelUtil.class);

	/**
     * Read all data from Excel sheet
     * @param filePath Path to Excel file
     * @param sheetName Name of the sheet
     * @return 2D array of data
     */
    public static Object[][] getExcelData(String filePath, String sheetName) {
        Object[][] data = null;
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet not found: " + sheetName);
                return new Object[0][0];
            }
            
            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();
            
            data = new Object[rowCount][colCount];
            
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i + 1);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i][j] = getCellValue(cell);
                }
            }
            
            logger.info("Excel data loaded from: " + filePath + ", Sheet: " + sheetName);
            
        } catch (IOException e) {
            logger.error("Failed to read Excel file: " + e.getMessage());
        }
        
        return data;
    }
    
    /**
     * Read data from Excel and return as List of Maps
     * @param filePath Path to Excel file
     * @param sheetName Name of the sheet
     * @return List of maps containing row data
     */
    public static List<Map<String, String>> getExcelDataAsMap(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet not found: " + sheetName);
                return dataList;
            }
            
            Row headerRow = sheet.getRow(0);
            int rowCount = sheet.getLastRowNum();
            
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String key = getCellValue(headerRow.getCell(j)).toString();
                    String value = getCellValue(row.getCell(j)).toString();
                    rowData.put(key, value);
                }
                
                dataList.add(rowData);
            }
            
            logger.info("Excel data loaded as map from: " + filePath);
            
        } catch (IOException e) {
            logger.error("Failed to read Excel file: " + e.getMessage());
        }
        
        return dataList;
    }
  
    /**
     * Get cell value based on cell type
     * @param cell Cell to read
     * @return Cell value as Object
     */
    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                	double numericValue = cell.getNumericCellValue();
                	if(numericValue == (long) numericValue)
                		return String.valueOf((long) numericValue);
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
