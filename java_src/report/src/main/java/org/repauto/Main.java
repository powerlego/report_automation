package org.repauto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

/**
 * Main class
 */
public class Main {

    private static final String SHEET_NAME = "2023 Raw";

    public static void main(String[] args) throws IOException {
        String userProfile = System.getenv("USERPROFILE");
        Path outCSV = Path.of(userProfile, "AppData", "Local", "Programs", "report_automation", "out.csv");
        Path path = Path.of(userProfile, "Desktop", "WTD Tracker Menards 2023.xlsx");
        List<List<String>> lines = new ArrayList<List<String>>();
        try (CSVReader reader = new CSVReader(new FileReader(outCSV.toFile()))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                List<String> lineList = new ArrayList<String>();
                for(String s : line) {
                    lineList.add(s.trim());
                }
                lines.add(lineList);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        FileInputStream fis = new FileInputStream(path.toFile());
        XSSFWorkbook workbook = new XSSFWorkbookFactory().create(fis);
        XSSFSheet sheet = null;
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            if (workbook.getSheetName(i).equals(SHEET_NAME)) {
                sheet = workbook.getSheetAt(i);
                break;
            }
        }
        List<String> header = lines.get(0);
        List<String> headerTemp = new ArrayList<String>();
        for (String s : header) {
            headerTemp.add(s.replace("_", " "));
        }
        lines.set(0, headerTemp);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        XSSFCellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        ProgressBar pb = new ProgressBarBuilder().setTaskName("Updating Excel").setInitialMax(lines.size())
                .setStyle(ProgressBarStyle.ASCII).setMaxRenderedLength(120).setUpdateIntervalMillis(10).build();
        boolean currency = false;
        if (sheet != null) {
            for (int i = 5; i < lines.size() + 5; i++) {
                List<String> line = lines.get(i - 5);
                if (sheet.getRow(i) == null) {
                    sheet.createRow(i);
                }
                for (int j = 0; j < line.size(); j++) {
                    currency = false;
                    String s = line.get(j);
                    if (s.contains("$") && i != 5) {
                        s = s.replace("$", "");
                        currency = true;
                    }
                    try {
                        int num = Integer.parseInt(s);
                        if (sheet.getRow(i).getCell(j) != null) {
                            sheet.getRow(i).getCell(j).setCellValue(num);
                        } else {
                            sheet.getRow(i).createCell(j).setCellValue(num);
                        }
                    } catch (NumberFormatException e) {
                        if (sheet.getRow(i).getCell(j) != null) {
                            sheet.getRow(i).getCell(j).setCellValue(s);
                        } else {
                            sheet.getRow(i).createCell(j).setCellValue(s);
                        }
                    }
                    if (currency && j == line.size() - 1 && i != 5) {
                        XSSFCellStyle moneyStyle2 = moneyStyle.copy();
                        moneyStyle2.setBorderRight(BorderStyle.THIN);
                        sheet.getRow(i).getCell(j).setCellStyle(moneyStyle2);
                    } else if (currency) {
                        sheet.getRow(i).getCell(j).setCellStyle(moneyStyle);
                    } else if (j == line.size() - 1 && i != 5) {
                        sheet.getRow(i).getCell(j).setCellStyle(style);
                    }
                }
                pb.step();
            }
        }
        pb.close();
        fis.close();
        FileOutputStream fos = new FileOutputStream(path.toFile());
        workbook.write(fos);
        workbook.close();
        fos.close();
    }
}
