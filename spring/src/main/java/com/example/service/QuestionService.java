package com.example.service;

import com.example.entity.Question;
import com.example.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public List<Question> getAllQuestions() {
        return questionMapper.findAll();
    }

    public Question getQuestionById(Integer id) {
        return questionMapper.findById(id);
    }

    public boolean addQuestion(Question question) {
        return questionMapper.insert(question) > 0;
    }

    public boolean updateQuestion(Question question) {
        return questionMapper.update(question) > 0;
    }

    public boolean deleteQuestion(Integer id) {
        return questionMapper.delete(id) > 0;
    }
}