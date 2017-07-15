/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.util;

import cn.edu.sdut.softlab.controller.Credentials;
import cn.edu.sdut.softlab.entity.Admin;
import cn.edu.sdut.softlab.entity.Level;
import cn.edu.sdut.softlab.entity.Student;
import cn.edu.sdut.softlab.entity.Teacher;
import cn.edu.sdut.softlab.qualifiers.LoggedIn;
import cn.edu.sdut.softlab.qualifiers.Preferred;
import cn.edu.sdut.softlab.service.AdminFacade;
import cn.edu.sdut.softlab.service.StudentFacade;
import cn.edu.sdut.softlab.service.TeacherFacade;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 *
 * @author huanlu
 */
@RequestScoped
public class UserProducers {

    public UserProducers() {
        System.out.print("UserProducers constructor called");
    }
    
    @Inject
    Logger logger;

    @Inject
    @Default
    Credentials credentials;
    
    @Inject
    @Default
    AdminFacade adminService;
    
    @Inject
    TeacherFacade teacherService;
    
    @Inject
    StudentFacade studentService;
    
    String userType = credentials.getLevel();
    
    @Produces
    @Preferred
    @SessionScoped
    public Level getUser(Admin admin, Teacher teacher, Student student){
        switch(userType){
            case "Admin":
                return admin = adminService.findByIdAndPassword(credentials.getNO().intValue(), credentials.getPassword());
            case "Teacher":
                return teacher = teacherService.findByTeacherNoAndPassword(credentials.getNO(), credentials.getPassword());
            case "Student":
                return student = studentService.findByStuNOAndPassword(credentials.getNO(), credentials.getPassword());
            default:
                return null;
        }
    }
    
}
