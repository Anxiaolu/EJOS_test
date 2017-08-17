/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.sdut.softlab.controller;

import cn.edu.sdut.softlab.entity.Record;
import cn.edu.sdut.softlab.service.QuestionFacade;
import cn.edu.sdut.softlab.service.RecordFacade;
import cn.edu.sdut.softlab.util.DateUtil;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.UserTransaction;

/**
 *
 * @author huanlu
 */
@RequestScoped
@Named(value = "recordController")
public class RecordController {
    
    @Inject
    Logger logger;
    
    @Inject
    RecordFacade recordService;
    
    @Inject
    QuestionFacade questionService;
    
    @Inject
    DateUtil dateUtil;
    
    @Inject
    UserTransaction utx;
    
    /**
     * 对照记录表中的未通过记录的时间和题目表的截止时间获取未完成题目的记录
     * @return
     * @throws Exception 
     */
    public List<Record> getRecordsLessFour() throws Exception{
        try {
            utx.begin();
            List<Record> currentrecords = recordService.findRecordsByStatus("未完成");
            List<Record> allowRecords = null;
            for (Record currentrecord : currentrecords) {
                if (dateUtil.getTwoDay(questionService.findQuestionsById(currentrecord.getQuestionId()).getDeadline()
                        ,currentrecord.getTime())<=4) {
                    allowRecords.add(currentrecord);
                }
            }
            return allowRecords;
        } finally{
            utx.commit();
        }
    }
}
