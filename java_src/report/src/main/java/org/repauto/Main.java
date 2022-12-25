package org.repauto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
                for (String s : line) {
                    lineList.add(s.trim());
                }
                lines.add(lineList);
            }
        }
        catch (IOException | CsvValidationException e) {
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
        XSSFCellStyle hStyle = workbook.createCellStyle();
        hStyle.setBorderBottom(BorderStyle.THIN);
        hStyle.setBorderRight(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        XSSFCellStyle moneyStyle = workbook.createCellStyle();
        int moneyFormat = BuiltinFormats.getBuiltinFormat("\"$\"#,##0_);[Red](\"$\"#,##0)");
        moneyStyle.setDataFormat(moneyFormat);
        XSSFCellStyle moneyStyle2 = moneyStyle.copy();
        moneyStyle2.setBorderRight(BorderStyle.THIN);
        int salesFormat = BuiltinFormats.getBuiltinFormat("#,##0_);[Red](#,##0)");
        XSSFCellStyle salesStyle = workbook.createCellStyle();
        salesStyle.setDataFormat(salesFormat);

        boolean currency = false;
        Pattern p = Pattern.compile("\\(\\d+\\)");
        if (sheet != null) {
            int colSize = lines.get(0).size();
            // Clear all data
            ProgressBar pb = new ProgressBarBuilder().setTaskName("Clearing Old Data")
                    .setInitialMax(sheet.getLastRowNum() - 6)
                    .setStyle(ProgressBarStyle.ASCII).setMaxRenderedLength(120).setUpdateIntervalMillis(10).build();
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 6; i < sheet.getLastRowNum(); i++) {
                for (int j = 0; j < colSize; j++) {
                    if (sheet.getRow(i).getCell(j) != null) {
                        sheet.getRow(i).getCell(j).setCellType(CellType.BLANK);
                    }
                }
                pb.step();
            }
            pb.close();
            pb = new ProgressBarBuilder().setTaskName("Updating Excel").setInitialMax(lines.size())
                    .setStyle(ProgressBarStyle.ASCII).setMaxRenderedLength(120).setUpdateIntervalMillis(10).build();
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
                    Matcher m = p.matcher(s);
                    if (m.matches()) {
                        s = s.replace("(", "-");
                        s = s.replace(")", "");
                    }
                    if (j == 5) {
                        s = "|" + s;
                    }
                    try {
                        int num = Integer.parseInt(s);
                        if (sheet.getRow(i).getCell(j) == null) {
                            sheet.getRow(i).createCell(j).setCellValue(num);
                        }
                        else {
                            sheet.getRow(i).getCell(j).setCellValue(num);
                        }
                    }
                    catch (NumberFormatException e) {
                        if (s.contains("|")) {
                            s = s.replace("|", "");
                        }
                        if (sheet.getRow(i).getCell(j) == null) {
                            sheet.getRow(i).createCell(j).setCellValue(s);
                        }
                        else {
                            sheet.getRow(i).getCell(j).setCellValue(s);
                        }
                    }
                    if (currency && j == line.size() - 1 && i != 5) {
                        sheet.getRow(i).getCell(j).setCellStyle(moneyStyle2);
                        currency = false;
                    }
                    else if (currency) {
                        sheet.getRow(i).getCell(j).setCellStyle(moneyStyle);
                        currency = false;
                    }
                    else if (j == line.size() - 1 && i != 5) {
                        sheet.getRow(i).getCell(j).setCellStyle(style);
                    }
                    else if (j == line.size() - 2 && i != 5) {
                        sheet.getRow(i).getCell(j).setCellStyle(salesStyle);
                    }
                    else if (j == line.size() - 1 && i == 5) {
                        sheet.getRow(i).getCell(j).setCellStyle(hStyle);
                    }
                }
                pb.step();
            }
            pb.close();

            pb = new ProgressBarBuilder().setTaskName("Fixing Separator").setInitialMax(lastRowNum - 6 - lines.size())
                    .setStyle(ProgressBarStyle.ASCII).setMaxRenderedLength(120).setUpdateIntervalMillis(10).build();
            for (int i = lines.size(); i < lastRowNum; i++) {
                if (sheet.getRow(i) == null) {
                    sheet.createRow(i);
                }
                XSSFCell cell = sheet.getRow(i).createCell(8);
                cell.setCellValue("");
                cell.setCellStyle(style);
                pb.step();
            }
            pb.close();
            // Auto size columns
            for (int i = 0; i < colSize; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        workbook.setForceFormulaRecalculation(true);
        fis.close();

        FileOutputStream fos = new FileOutputStream(path.toFile());
        workbook.write(fos);
        workbook.close();
        fos.close();
    }
}
