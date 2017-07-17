/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.controller;

import cn.edu.sdut.softlab.entity.Teacher;
import cn.edu.sdut.softlab.service.TeacherFacade;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

/**
 *
 * @author huanlu
 */
@ManagedBean(name = "teacherController")
@RequestScoped
public class TeacherController {
    
    @Inject
    Logger logger;
    
    @Inject
    TeacherFacade teacherService;
    
    public List<Teacher> getAll(){
        return teacherService.findAll();
    }
    
    public void modify(){
        
    }
}
