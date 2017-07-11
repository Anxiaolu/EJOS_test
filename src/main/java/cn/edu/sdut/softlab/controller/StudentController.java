/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.controller;

import cn.edu.sdut.softlab.entity.Student;
import cn.edu.sdut.softlab.service.StudentFacade;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.UserTransaction;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author huanlu
 */
@RequestScoped
@Named("studentcontroller")
public class StudentController {
    
    @Inject
    Logger logger;

    @Inject
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Inject
    StudentFacade studentSerivce;

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
        editStudent.setName("gaosan");
        logger.info("Student Edit!~~~~~~~~~~~~~~~~~~~~" + editStudent.getId() + "Name:" + editStudent.getName());
        try {
            utx.begin();
            em.merge(editStudent);
        } finally {
            utx.commit();
            FacesMessage fms = new FacesMessage("Student modify", event.getObject().getClass().getName());
            FacesContext.getCurrentInstance().addMessage(null, fms);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Student) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
    }

}
