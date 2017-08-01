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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
            File f = new File("/home/huanlu/" + path + studentService.findByStuId(stu_team_id).getName() + ".csv");
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

    public ResultSetMetaData getResultSetMetaData() throws Exception {
        String sql = "select * from teacher";
        PreparedStatement ps = null;
        try {
            ps = studentService.getEm().unwrap(Connection.class).prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(CSVUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet resultSet = null;
        try {
            resultSet = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CSVUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet.getMetaData();
    }
    
    
    public void outOfCsv(String filePath, String sql) throws Exception {
        // 判断文件是否存在,存在则删除,然后创建新表格
        File tmp = new File(filePath);
        if (tmp.exists()) {
            if (tmp.delete()) {
                logger.info(filePath);
            }
        }
        
//        Query query = studentService.getHibernateQuery(sql);
//        
//        query.setResultTransformer(
//                //ToListResultTransformer.INSTANCE
//                new ResultTransformer() {
//            @Override
//            public Object transformTuple(Object[] tuple, String[] aliases) {
//                Integer id = (Integer) tuple[0];
//                return ;
//            }
//
//            @Override
//            public List transformList(List collection) {
//                return Collections.unmodifiableList(collection);
//            }
//        }
//        );

        // 创建CSV写对象
        CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));

        // 数据查询开始
        PreparedStatement preparedStatement = studentService.getEm().unwrap(Connection.class).prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        // 获取结果集表头
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        logger.info("返回结果字段个数:" + columnCount);

        // 文件输出
        csvWriter.flush();
        csvWriter.close();
    }
    
}
