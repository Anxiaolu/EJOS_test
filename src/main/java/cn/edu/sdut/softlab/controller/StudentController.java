/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.controller;

import cn.edu.sdut.softlab.entity.Student;
import cn.edu.sdut.softlab.entity.Team;
import cn.edu.sdut.softlab.entity.User;
import cn.edu.sdut.softlab.service.StudentFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author huanlu
 */
@RequestScoped
@Named("stuController")
public class StudentController {

    @Inject
    Logger logger;

    @Inject
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Inject
    StudentFacade studentSerivce;

    private Student currentstu = new Student(new Team(1));

    public Student getCurrentstu() {
        return currentstu;
    }

    public void setCurrentstu(Student currentstu) {
        this.currentstu = currentstu;
    }

    public List<Student> getAll() throws Exception {
        try {
            utx.begin();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Student.class));
            return em.createQuery(cq).getResultList();
        } finally {
            utx.commit();
        }
    }

    public void onRowEdit(RowEditEvent event) throws Exception {
        Student editStudent = (Student) event.getObject();
        currentstu.setId(editStudent.getId());
        logger.log(Level.INFO, "Student Edit!~~~~~~~~~~~~~~~~~~~~{0}", editStudent.toString());
        logger.log(Level.INFO, "current information:    {0}", currentstu.toString());
        try {
            utx.begin();
            em.merge(currentstu);
        } finally {
            utx.commit();
            FacesMessage fms = new FacesMessage("Student modify", event.getObject().getClass().getName());
            FacesContext.getCurrentInstance().addMessage(null, fms);
            currentstu = null;
            event = null;
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((User) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        currentstu = null;
    }

    public void modify() throws Exception {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Student loginStudent = (Student) session.getAttribute("currentUser");
        loginStudent.toString();
        currentstu.setStudentNum(loginStudent.getStudentNum());
        try {
            utx.begin();
            em.merge(currentstu);
            logger.log(Level.INFO, "Student Edit:{0}", currentstu.toString());
        } finally {
            currentstu = null;
            utx.commit();
        }
    }

    public String modifyMySelf(Student loginStudent) throws Exception {
        logger.log(Level.INFO, "Student information modify:{0}", loginStudent.toString());
        utx.begin();
        currentstu.setId(loginStudent.getId());
        currentstu.setStudentNum(loginStudent.getStudentNum());
        em.merge(currentstu);
        utx.commit();
        return "";
    }

    public void delete(Student stu) throws Exception {
        logger.log(Level.INFO, "{0}", stu.toString());
        Student delectStu = studentSerivce.findByStuId(stu.getId());
        try {
            utx.begin();
            logger.log(Level.INFO, "Student Delete Called:{0}", delectStu.toString());
            em.remove(delectStu);
            em.flush();
        }catch(NotSupportedException | SystemException e){
            throw new RuntimeException(e);
        } 
        finally {
            utx.commit();
        }
    }

}
