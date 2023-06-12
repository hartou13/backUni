package pdf;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import gdao.inherit.DBModel;

public class PdfGen {
    public static void genPdf(String filename, DBModel mod) throws Exception {
        String dest = "./src/main/resources/static/" + filename;
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        float[] pointColumnWidth = new float[2];
        for (int i = 0; i < pointColumnWidth.length; i++) {
            pointColumnWidth[i] = 150F;
        }
        Table table = new Table(pointColumnWidth);

        ArrayList<String> lines = mod.getColumnsNames();
        HashMap<String, Object> details = mod.listColumnValues();

        for (int i = 0; i < lines.size(); i++) {
            table.addCell(lines.get(i));
            Object data = details.get(lines.get(i));
            if (data == null)
                table.addCell(new Cell().add(""));
            else
                table.addCell(new Cell().add(data.toString()));
        }

        document.add(table);

        document.close();
    }

    public static void genPdfLst(String filename, ArrayList<DBModel> mod) throws Exception {
        String dest = "./src/main/resources/static/" + filename;
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        ArrayList<String> lines = mod.get(0).getColumnsNames();
        // HashMap<String, Object> details = mod.get(0).listColumnValues();
        
        float[] headerColumn={150f};
        Table header=new Table(headerColumn);
        header.addCell(mod.get(0).getClass().getSimpleName());

        float[] pointColumnWidth = new float[lines.size()];
        for (int i = 0; i < pointColumnWidth.length; i++) {
            pointColumnWidth[i] = 150F;
        }
        Table table = new Table(pointColumnWidth);

        for (int i = 0; i < lines.size(); i++) {
            table.addCell(lines.get(i));
        }

        for (int i = 0; i < mod.size(); i++) {

            HashMap<String, Object> details = mod.get(i).listColumnValues();
            for (int j = 0; j < lines.size(); j++) {
                Object data = details.get(lines.get(j));
                System.out.println(data);
                if (data == null)
                    table.addCell(new Cell().add(""));
                else
                    table.addCell(new Cell().add(data.toString()));
            }
        }
        document.add(header);
        document.add(table);

        document.close();
    }
}
