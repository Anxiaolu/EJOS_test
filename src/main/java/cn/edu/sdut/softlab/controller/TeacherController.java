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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author huanlu
 */
@ManagedBean(name = "teaController")
@RequestScoped
public class TeacherController {
    
    @Inject
    Logger logger;
    
    @Inject
    TeacherFacade teacherService;
    
    @Inject
    UserTransaction utx;
    
    @Inject
    EntityManager em;
    
    private List<Teacher> filterTeachers;

    public List<Teacher> getFilterTeachers() {
        return filterTeachers;
    }

    public void setFilterTeachers(List<Teacher> filterTeachers) {
        this.filterTeachers = filterTeachers;
    }
    
    private Teacher currentTea = null;

    public Teacher getCurrentTea() {
        return currentTea;
    }

    public void setCurrentTea(Teacher currentTea) {
        this.currentTea = currentTea;
    }
    
    public List<Teacher> findAll(){
        return teacherService.findAll();
    }
    
    private String deletename;

    public String getDeletename() {
        return deletename;
    }

    public void setDeletename(String deletename) {
        this.deletename = deletename;
    }

    
    public void onRowEdit(RowEditEvent event) throws Exception {
        Teacher editTeacher = (Teacher) event.getObject();
        currentTea.setId(editTeacher.getId());
        logger.info("Student Edit!~~~~~~~~~~~~~~~~~~~~" + editTeacher.toString());
        logger.info("current information:    " + currentTea.toString());
        try {
            utx.begin();
            em.merge(currentTea);
        } finally {
            utx.commit();
            FacesMessage fms = new FacesMessage("Student modify", event.getObject().getClass().getName());
            FacesContext.getCurrentInstance().addMessage(null, fms);
            currentTea = null;
            event = null;
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Teacher) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        currentTea = null;
    }
    
    public void delete() throws Exception {
        logger.info(deletename + " -----");
        Teacher deleteTeacher = teacherService.findByTeaName(deletename);
        logger.info(deleteTeacher.toString() + "-------------------------------------------------------------");
        try {
            utx.begin();
            em.remove(deleteTeacher);
        } finally {
            utx.commit();
            logger.info("Teacher Delete Called:" + deleteTeacher.toString());
        }
    }
}
