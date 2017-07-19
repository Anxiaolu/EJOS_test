/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.util;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import static jdk.nashorn.internal.objects.NativeRegExp.test;
import org.junit.Test;


/**
 *
 * @author huanlu
 */
public class CSVUtil {
    
    @Inject
    Logger logger;
    
//    public static String TAB = "\r\n";
//
//    static {
//        Properties prop = System.getProperties();
//        String os = prop.getProperty("os.name").toLowerCase();
//        if (os.startsWith("win")) {
//            TAB = "\r\n";
//        } else if (os.startsWith("linux") || os.startsWith("unix")) {
//            TAB = "\n";
//        } else if (os.startsWith("mac")) {
//            TAB = "\r";
//        }
//    }
//    
//    public void exportCsv(HttpServletResponse response, List<String> titles, List<List<String>> data, String fileName) {
//        StringBuilder sb = new StringBuilder();
//        OutputStream outputStream = null;
//        for (int i = 0; i < titles.size(); i++) {
//            if (i != titles.size() - 1) {
//                sb.append(titles.get(i)).append(",");
//            } else {
//                sb.append(titles.get(i)).append(TAB);
//            }
//        }
//        for (int i = 0; i < data.size(); i++) {
//            List<String> row = data.get(i);
//            for (int j = 0; j < row.size(); j++) {
//                if (j != row.size() - 1) {
//                    sb.append(row.get(j)).append(",");
//                } else {
//                    sb.append(row.get(j)).append(TAB);
//                }
//            }
//        }
//
//        try {
//
//            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "iso8859-1"));
//            //response.addHeader("Content-Length", "" + sb.length());  
//            //response.setContentType("application/csv;charset=UTF-8"); 
//            response.setContentType("multipart/form-data");//设置文件ContentType类型，这样设置，会自动判断下载文件类型
//            outputStream = response.getOutputStream();
//
//            outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});//加上bom头，才不会中文乱码     
//            outputStream.write(sb.toString().getBytes("UTF-8"));
//            outputStream.flush();
//        } catch (IOException e) {
//            logger.log(Level.INFO, "CSVUtils.exportCsv error:{0}", e);
//        } finally {
//            try {
//                outputStream.close();
//            } catch (IOException e) {
//                logger.log(Level.INFO, "CSVUtils.exportCsv close OutputStream error:{0}", e);
//            }
//        }
//    }
//
//    public List<String> importCsv(File file) {
//        List<String> data = new ArrayList<>();
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new FileReader(file));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                data.add(line);
//            }
//        } catch (IOException e) {
//            logger.log(Level.INFO, "CSVUtils.importCsv error:{0}", e);
//        } finally {
//            try {
//                br.close();
//            } catch (IOException e) {
//                logger.log(Level.INFO, "CSVUtils.importCsv close BufferedReader error:{0}", e);
//            }
//        }
//
//        return data;
//    }
//
//    public List<String> importCsv(InputStream inputStream) {
//        List<String> data = new ArrayList<>();
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new InputStreamReader(inputStream));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                data.add(line);
//            }
//
//        } catch (IOException e) {
//            logger.log(Level.INFO, "CSVUtils.importCsv error:{0}", e);
//        } finally {
//            try {
//                br.close();
//                inputStream.close();
//            } catch (IOException e) {
//                logger.log(Level.INFO, "CSVUtils.importCsv close BufferedReader or InputStream error:{0}", e);
//            }
//        }
//        return data;
//    }
    
    public static void read(String filePath) throws FileNotFoundException, IOException {
        try {

            CsvReader csvReader = new CsvReader(filePath);
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                System.out.print(csvReader.getRawRecord());
                System.out.print(csvReader.get("用户名"));
            }
        } catch (IOException e) {
        }
    }
    
    @Test
    public static void main(String[] args) throws IOException {
        read("/home/huanlu/Users.csv");
    }
    
}
