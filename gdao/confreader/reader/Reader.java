/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdao.confreader.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Hart
 */
public class Reader {
    public HashMap<String,String> read(String filename) throws FileNotFoundException{
        File file=new File(filename);
        return this.read(file);
    }
    
     public HashMap<String,String> read(File file) throws FileNotFoundException{
        Scanner sc=new Scanner(file);
        HashMap<String,String> res=new HashMap<>();
        while(sc.hasNextLine()){
            String line=sc.nextLine();
            String [] part=line.split("=");
            res.put(part[0], part[1]);
        }
        return res;
    }
     
}
