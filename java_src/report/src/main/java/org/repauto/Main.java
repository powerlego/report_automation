package org.repauto;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

/**
 * Main class
 */
public class Main 
{
    public static void main( String[] args ) throws IOException
    {
        String userProfile = System.getenv("USERPROFILE");
        Path path = Path.of(userProfile, "Desktop", "WTD Tracker Menards 2023.xlsx");
        System.out.println(path);
        FileInputStream file = new FileInputStream(path.toFile());
        XSSFWorkbook workbook = new XSSFWorkbookFactory().create(file);
        for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
            System.out.println(workbook.getSheetName(i));
        }
        workbook.close();
    }
}
