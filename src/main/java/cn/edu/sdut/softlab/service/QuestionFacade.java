package cn.edu.sdut.softlab.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import cn.edu.sdut.softlab.entity.Question;

/**
 * @author huanlu
 */
@Named("questionService")
public class QuestionFacade extends AbstractFacade<Question> {

    public QuestionFacade() {
        super(Question.class);
    }

    public List<Question> findQuestionsByTeam(Integer teamId) {
        Map<String, Object> parameters = new HashMap<>(0);
        parameters.put("teamId", teamId);
        return findByNamedQuery("Question.findByTeamId", parameters);
    }

    public Question findQuestionsById(Integer ques_id) {
        Map<String, Object> parameters = new HashMap<>(0);
        parameters.put("id", ques_id);
        return findSingleByNamedQuery("Question.findById", parameters, Question.class).get();
    }
    
    public Question findQuestionsByName(String name) {
        Map<String, Object> parameters = new HashMap<>(0);
        parameters.put("question", name);
        return findSingleByNamedQuery("Question.findByQuestion", parameters, Question.class).get();
    }

}
