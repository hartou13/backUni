package xcl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.*;

public class Xcl {
    public static void genXcl(String filename, Object[] toPlace)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        Workbook workbook = new XSSFWorkbook();
        // feuille
        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);
        ArrayList<String> li = Util.getColumnsNames(toPlace[0]);
        for (int i = 0; i < li.size(); i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(li.get(i));
        }
        for (int i = 0; i < toPlace.length; i++) {
            Row line = sheet.createRow(i + 1);
            HashMap<String, Object> tab = Util.listColumnValues(toPlace[i]);
            for (int j = 0; j < li.size(); j++) {
                Cell linCell = line.createCell(j);
                if (tab.get(li.get(j)) != null)
                    linCell.setCellValue(tab.get(li.get(j)).toString());
                else
                    linCell.setCellValue("");
            }
        }
        File currDir = new File("./src/main/resources/static/");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length()) +"/"+ filename + ".xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }
    // public static void main(String[] args) throws Exception {
    // // workbook
    // Workbook workbook = new XSSFWorkbook();
    // // feuille
    // Sheet sheet = workbook.createSheet("Persons");
    // sheet.setColumnWidth(0, 6000);
    // sheet.setColumnWidth(1, 4000);
    // // ligne numero 0
    // Row header = sheet.createRow(0);
    // // style cellule
    // CellStyle headerStyle = workbook.createCellStyle();
    // // couleur
    // headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    // headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    // // police
    // XSSFFont font = ((XSSFWorkbook) workbook).createFont();
    // font.setFontName("Arial");
    // font.setFontHeightInPoints((short) 16);
    // font.setBold(true);
    // headerStyle.setFont(font);
    // // cellule numero 0
    // Cell headerCell = header.createCell(0);
    // headerCell.setCellValue("Name");
    // headerCell.setCellStyle(headerStyle);

    // // cellule numero 1
    // headerCell = header.createCell(1);
    // headerCell.setCellValue("Age");
    // headerCell.setCellStyle(headerStyle);

    // //cellule style
    // CellStyle style = workbook.createCellStyle();
    // style.setWrapText(true);
    // //ligne 2
    // Row row = sheet.createRow(2);
    // //colonne 0
    // Cell cell = row.createCell(0);
    // cell.setCellValue("John Smith");
    // cell.setCellStyle(style);

    // //colonne 1
    // cell = row.createCell(1);
    // cell.setCellValue(20);
    // cell.setCellStyle(style);

    // File currDir = new File(".");
    // String path = currDir.getAbsolutePath();
    // String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

    // FileOutputStream outputStream = new FileOutputStream(fileLocation);
    // workbook.write(outputStream);
    // workbook.close();
    // // ArrayList<Vehicule> vehic = new Vehicule().listAll();
    // // System.out.println(new Gson().toJson(vehic.get(0)));
    // // PdfGen.genPdf("test.pdf", vehic.get(0));
    // // PdfGen.genPdfLst("test2.pdf", (ArrayList)vehic);

    // SpringApplication.run(KiloApplication.class, args);

    // }
}
