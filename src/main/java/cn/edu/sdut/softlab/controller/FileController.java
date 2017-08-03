/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author huanlu
 */
@ManagedBean(name = "fileController")
@ApplicationScoped
public class FileController {
    
    @Inject
    Logger logger;
    
    public String getWildflyRootPath(){
        return System.getProperty("user.dir");
    }

    public void getUploadFile(FileUploadEvent event) {
        String filename= event.getFile().getFileName();
        String filecontenttype = event.getFile().getContentType();
        UploadedFile file =  event.getFile();
        logger.info(file.toString());
    }
}
