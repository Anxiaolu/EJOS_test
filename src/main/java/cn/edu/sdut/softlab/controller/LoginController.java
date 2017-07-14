package cn.edu.sdut.softlab.controller;

import cn.edu.sdut.softlab.entity.Level;
import cn.edu.sdut.softlab.entity.Admin;
import cn.edu.sdut.softlab.entity.Student;
import cn.edu.sdut.softlab.entity.Teacher;
import cn.edu.sdut.softlab.qualifiers.LoggedIn;
import cn.edu.sdut.softlab.service.AdminFacade;
import cn.edu.sdut.softlab.service.StudentFacade;
import cn.edu.sdut.softlab.service.TeacherFacade;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("login")
public class LoginController implements Serializable {

    private static final long serialVersionUID = 7965455427888195913L;

    private String level = null;

    @Inject
    Logger logger;

    @Inject
    private Credentials credentials;

    @Inject
    AdminFacade adminService;

    @Inject
    TeacherFacade teacherService;

    @Inject
    StudentFacade studentService;

    @Inject
    FacesContext facesContext;

    private Level currentUser = null;

    @Produces
    @LoggedIn
    public Level getCurrentUser() {
        return this.currentUser;
    }
    
    @Produces
    @LoggedIn
    public void check(Level level){
        if (level != null) {
            currentUser = level;
            logger.info("Login:" + currentUser.toString());
            facesContext.addMessage(null, new FacesMessage("Welcome, " + currentUser.getName()));
        }
    }

    /**
     * 处理登录逻辑.
     */
    public String login() {
        logger.info("Login Level:" + credentials.getLevel() + "-------------");
        switch (credentials.getLevel()) {
            case "Admin":
                Admin admin = adminService.findByIdAndPassword(credentials.getNO().intValue(),credentials.getPassword());
                check(admin);
                break;
            case "Teacher":
                Teacher teacher = teacherService.findByTeacherNoAndPassword(credentials.getNO(), credentials.getPassword());
                check(teacher);
                break;
            case "Student":
               Student student = studentService.findByStuNOAndPassword(credentials.getNO(), credentials.getPassword());
                check(student);
                break;
        }
        return "/home.xhtml?faces-redirect=true";
    }

    /**
     * 处理退出登录逻辑.
     */
    public void logout() {
        facesContext.addMessage(null, new FacesMessage("Goodbye, " + currentUser.getName()));
        logger.info("LogOut:" + currentUser.getName() + "--------------");
        currentUser = null;
    }

    /**
     * 判断用户是否登录.
     *
     * @return true：已经登录；false：没有登录
     */
    public boolean isLoggedIn() {
        return currentUser != null;//才看明白，null != null 没登录！
    }

    public void checkLogin(ComponentSystemEvent event) {
        if (!this.isLoggedIn()) {
            FacesContext context = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            handler.performNavigation("login");
        }
    }

    public void checkLoginExt(ComponentSystemEvent event) {
        if (!this.isLoggedIn()) {
            FacesContext context = FacesContext.getCurrentInstance();

            ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            handler.performNavigation("../login");
        }
    }

    /**
     * 异步根据前台绑定的根据登录学生的id查询,返回对应的
     *
     * @param event
     */
//    public String profileReturn(ActionEvent event) {
//        Integer id = (Integer) event.getComponent().getAttributes().get("loginid");
//        Student loginStudent = studentService.findByStuId(id);
//        logger.info("Student:" + loginStudent.toString());
//        return "/student/student_modify.jsf?id=" + id + "&name = " + loginStudent.getName();
//    }
//    public void studentModifyLogin(Integer id, String name) {
//        Student checkStu = studentService.findByStuId(id);
//        if (name.equals(checkStu.getName())) {
//            
//        }
//    }
}