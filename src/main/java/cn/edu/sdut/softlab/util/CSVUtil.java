/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.util;

import cn.edu.sdut.softlab.entity.Student;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.junit.Test;
import org.hibernate.transform.ResultTransformer;

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

    public ResultSetMetaData getResultSetMetaData() throws Exception {
        String sql = "select * from teacher";
        PreparedStatement ps = null;
        try {
            ps = em.unwrap(Connection.class).prepareStatement(sql);
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
    
    public void getStudentsByTeam(){
        Session session = em.unwrap(Session.class);
        Student stu = em.find(Student.class, (Object)1);
        org.hibernate.Query query = session.createFilter(stu.getTeam(),"where this.team.id = :stu_team_id");
        query.setParameter("stu_team_id", 1);
        List<Student> stus = query.list();
        stus.forEach((s) -> {
            System.out.println("cn.edu.sdut.softlab.controller.StudentController.getStudentsByTeam()" + s.toString());
        });
    }
    
    public void outOfCsv(String filePath, String sql,Object stu_team_id) throws Exception {
        // 判断文件是否存在,存在则删除,然后创建新表格
        File tmp = new File(filePath);
        if (tmp.exists()) {
            if (tmp.delete()) {
                logger.info(filePath);
            }
        }

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
        PreparedStatement preparedStatement = em.unwrap(Connection.class).prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        // 获取结果集表头
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        logger.info("返回结果字段个数:" + columnCount);

        // 文件输出
        csvWriter.flush();
        csvWriter.close();
    }
    
    @Test
    public static void main(String[] args) throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("labUnit");
        EntityManager em = factory.createEntityManager();
        //UserTransaction transaction = (UserTransaction) em.getTransaction();
        //transaction.begin();
        //read("/home/huanlu/Users.csv", ',', true);
        //headerSort(em);
        //transaction.commit();
        em.close();
        factory.close();
    }
}
