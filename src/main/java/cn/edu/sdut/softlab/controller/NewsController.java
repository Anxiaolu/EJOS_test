/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.controller;

import cn.edu.sdut.softlab.entity.News;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.UserTransaction;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author huanlu
 */
@ManagedBean(name = "newsController")
@ApplicationScoped
public class NewsController {
    
    @Inject
    Logger logger;
    
    @Inject
    UserTransaction utx;
    
    @Inject
    EntityManager em;
    
    @Inject
    FacesContext facesContext;
    
    private News currentnews = new News();

    public News getCurrentnews() {
        return currentnews;
    }

    public void setCurrentnews(News currentnews) {
        this.currentnews = currentnews;
    }
    
    public List<News> getAll() throws Exception{
        try {
            utx.begin();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(News.class));
            return em.createQuery(cq).getResultList();
        } finally{
            utx.commit();
        }
    }
    
    public void addSingleNews() throws Exception{
        currentnews.setStatus("未完成");
        try {
            utx.begin();
            em.persist(currentnews);
            facesContext.addMessage(null, new FacesMessage("添加成功"));
        }finally{
            utx.commit();
        }
    }
    
    public void onRowEdit(RowEditEvent event) throws Exception {
        News editNews = (News) event.getObject();
        currentnews.setId(editNews.getId());
        logger.log(Level.INFO, "News Edit!~~~~~~~~~~~~~~~~~~~~{0}", editNews.toString());
        logger.log(Level.INFO, "current information:    {0}", currentnews.toString());
        try {
            utx.begin();
            em.merge(currentnews);
        } finally {
            utx.commit();
            FacesMessage fms = new FacesMessage("News modify", event.getObject().getClass().getName());
            FacesContext.getCurrentInstance().addMessage(null, fms);
            currentnews = null;
            event = null;
        }
    }
    
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((News)event.getObject()).getTitle());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        currentnews = null;
    }
}
