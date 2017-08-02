/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.util;

import cn.edu.sdut.softlab.entity.Student;
import cn.edu.sdut.softlab.service.StudentFacade;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author huanlu
 */
@Named(value = "csvUtil")
@ApplicationScoped
public class CSVUtil {

    @Inject
    Logger logger;

    @Inject
    StudentFacade studentService;
    
    @Inject
    EntityManager em;

    /**
     *
     * @param filePath csv文件的路径
     * @param delimiter 选择csv文件的分隔符
     * @param isNeedHeader 选择是否读取表头
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void read(String filePath, char delimiter, boolean isNeedHeader) throws FileNotFoundException, IOException {
        try {
            CsvReader csvReader = new CsvReader(filePath, delimiter, Charset.forName("UTF-8"));

            //选择是否读取表头
            if (isNeedHeader) {
            } else {
                csvReader.readHeaders();
            }
            while (csvReader.readRecord()) {//逐行读入数据
                System.out.print(csvReader.getRawRecord());
                System.out.print(csvReader.get("用户名"));
            }
            csvReader.close();
        } catch (IOException e) {
        }
    }
    
    public Object[] getFieldValues(Object object) {
        if (object == null)
            return null;
        Field[] fields = object.getClass().getDeclaredFields();
        List<Object> fieldValueList = new ArrayList<Object>();
        //Map<String, Object> fieldValueMap = new HashMap<String, Object>();

        try {
            for (Field f : fields) {
                if (f.getModifiers() > 2) {
                    continue;
                }
                fieldValueList.add(f.get(object));
                //fieldValueMap.put(f.getName(), f.get(object));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return fieldValueList.toArray();
        //return fieldValueMap.values().toArray();
    }
    
    public String[] toStrings(Student s){
        String[] stu = {Integer.toString(s.getId()),s.getName(),s.getPassword()
                ,s.getIdCard(),s.getStudentNum().toString(),s.getTeam().getName()};  
        return stu;
    }
    
    /**
     * 
     * @param path  生成csv文件所选路径
     * @param delimiter 选择分隔符
     * @param stu_team_name 确定生成文件的名称
     * @throws Exception 
     */
    public void writeWithStudentByTeam(String path,char delimiter,Integer stu_team_id) throws Exception{
        try {
            File f = new File("/home/huanlu/" + path + "/" + studentService.findByStuId(stu_team_id).getTeam().getName() + ".csv");
            OutputStream output = new FileOutputStream(f);
            CsvWriter csvWriter = new CsvWriter(output, delimiter, Charset.forName("UTF-8"));
            List<Student> stus = studentService.findByTeam(stu_team_id);
            for (Student s:stus) {
                csvWriter.writeRecord(this.toStrings(s));
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSetMetaData getResultSetMetaData(String table_name) throws Exception {
        String sql = "select * from " + table_name;
        PreparedStatement ps = studentService.getEm().unwrap(Connection.class).prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        return resultSet.getMetaData();
    }
    
    /**
     * 获取数据表的表头信息
     * @param table_name
     * @return String[] 返回表头信息
     * @throws Exception 
     */
    public Map<String,Object> getColumnName(String table_name) throws Exception {
        String sql = "select * from " + table_name;
        PreparedStatement ps = em.unwrap(java.sql.Connection.class).prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        JSONArray columnName = new JSONArray();
        for(int i = 1; i <= columnCount; i++)
        {
            JSONObject object = new JSONObject();
            logger.info(md.getColumnName(i));
            columnName.add(object);
        }
        return null;
    }
    
    public void test(String table_name) throws SQLException{
        EntityManagerFactory emf = em.getEntityManagerFactory();
        Map<String,Object> emfproperties = emf.getProperties();
        //logger.info(emfproperties.toString());
        List<String> key = new ArrayList<>(emfproperties.keySet());
//        for (String s : key) {
//            System.out.println("000 " + s);
//        }
        org.hibernate.engine.spi.SessionImplementor sessionImp = (org.hibernate.engine.spi.SessionImplementor) em.getDelegate();
        DatabaseMetaData metadata = null;
        try {
            metadata = sessionImp.connection().getMetaData();
            logger.info(metadata.toString());
            ResultSet res = metadata.getIndexInfo("ejos", null, table_name, false, true);
            System.out.println("List of columns: ");
            while (res.next()) {                
                System.out.println("1 " + res.getString(1));
                System.out.println("2 " + res.getString(2));
                System.out.println("3 " + res.getString(3));
                System.out.println("4 " + res.getString(4));
                System.out.println("5 " + res.getString(5));
                System.out.println("6 " + res.getString(6));
                System.out.println("7 " + res.getString(7));
                System.out.println("8 " + res.getString(8));
                System.out.println("9 " + res.getString(9));
                System.out.println("10 " + res.getString(10));
            }
//            while (res.next()) {
//                System.out.println(
//                        "  " + res.getString("TABLE_SCHEM")
//                        + ", " + res.getString("TABLE_NAME")
//                        + ", " + res.getString("COLUMN_NAME")
//                        + ", " + res.getString("TYPE_NAME")
//                        + ", " + res.getInt("COLUMN_SIZE")
//                        + ", " + res.getInt("NULLABLE"));
//            }
            res.close();
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
