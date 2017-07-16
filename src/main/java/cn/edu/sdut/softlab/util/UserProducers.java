/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.util;

import cn.edu.sdut.softlab.controller.Credentials;
import cn.edu.sdut.softlab.entity.Admin;
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
import cn.edu.sdut.softlab.entity.User;
import java.io.Serializable;
import javax.inject.Named;

/**
 *
 * @author huanlu
 */
@Named
@RequestScoped
public class UserProducers implements Serializable{

    public UserProducers() {
        System.out.print("UserProducers constructor called");
    }
    
    @Inject
    Logger logger;

    @Inject
    @Default
    Credentials credentials;
    
    @Produces
    @Preferred
    @SessionScoped
    public User getUser(String level){
        switch(level){
            case "Admin": 
                return new Admin();
            case "Teacher":
                return new Teacher();
            case "Student":
                return new Student();
            default:
                return null;
        }
    }
    
}
