package CSVImport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import gdao.genericdao.GenericDAO;
import gdao.inherit.DBModel;
public class CSVProcessor<T extends DBModel<T, Integer>> {
    public static <T extends DBModel<T , Integer>> List<T> readCSV(MultipartFile csvFile, Class<T> clazz) {
        List<T> dataList = new ArrayList<>();
        try (InputStream inputStream = csvFile.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            // Detect the separator
            br.mark(1);
            if (br.read() != 0xFEFF) {
                br.reset();
            }
            String line = br.readLine();
            String separator = detectSeparator(line);
            // Skip the header line
            // br.readLine();
            String[] head=line.split(separator);
            for (int i = 0; i < head.length; i++) {
                System.out.println(head[i]);
            }
            while ((line = br.readLine()) != null) {
                String[] data = line.split(separator);
                T object = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < data.length; i++) {
                    String fieldValue = data[i].trim();
                    // Assuming the class has public fields with the same order as the CSV columns
                    System.out.println(head[i]);
                    java.lang.reflect.Field field = GenericDAO.getFieldOfString(clazz, head[i]);
                    Class<?> fieldType = field.getType();
                    if (fieldType == String.class) {
                        GenericDAO.setter(field).invoke(object, fieldValue);
                    } else if (fieldType == int.class || fieldType == Integer.class) {
                        int intValue = Integer.parseInt(fieldValue);
                        GenericDAO.setter(field).invoke(object, intValue);
                    } else if (fieldType == double.class || fieldType == Double.class) {
                        double doubleValue = Double.parseDouble(fieldValue);
                        GenericDAO.setter(field).invoke(object, doubleValue);
                    } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                        boolean value = Boolean.parseBoolean(fieldValue);
                        GenericDAO.setter(field).invoke(object, value);
                    }
                    
                    // Add more data types as needed
                }
                dataList.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
    private static String detectSeparator(String line) {
        // Count occurrences of potential separators
        int commaCount = countOccurrences(line, ",");
        int semicolonCount = countOccurrences(line, ";");
        int tabCount = countOccurrences(line, "\t");
        // Choose the separator with the highest count
        if (commaCount >= semicolonCount && commaCount >= tabCount) {
            return ",";
        } else if (semicolonCount >= commaCount && semicolonCount >= tabCount) {
            return ";";
        } else {
            return "\t";
        }
    }
    private static int countOccurrences(String line, String separator) {
        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = line.indexOf(separator, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += separator.length();
            }
        }
        return count;
    }
}
