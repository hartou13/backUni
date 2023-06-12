package com.kilometer.kilo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

// import com.google.api.services.storage.model.Notification;
import com.google.gson.Gson;

import gdao.inherit.DBModel;
import pdf.PdfGen;
import universalController.classmanaging.Classe;
import xcl.Xcl;

@SpringBootApplication(scanBasePackages = { "controller", "com.kilometer.kilo", "model.lot.sary" })
@CrossOrigin
public class KiloApplication {

	public static void main(String[] args) throws Exception {
		// System.out.println(new Produit().listAll());
		// Xcl.genXcl("test", new Produit().listAll().toArray());
		SpringApplication.run(KiloApplication.class, args);

	}

}
