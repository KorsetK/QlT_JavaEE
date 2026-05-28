package com.example.service;

import com.example.entity.Question;
import com.example.mapper.WrongBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WrongBookService {

    @Autowired
    private WrongBookMapper wrongBookMapper;

    public void recordWrongQuestion(Integer userId, Integer questionId) {
        wrongBookMapper.insert(userId, questionId);
    }

    public void removeWrongQuestion(Integer userId, Integer questionId) {
        wrongBookMapper.delete(userId, questionId);
    }

    public List<Question> getUserWrongQuestions(Integer userId) {
        return wrongBookMapper.findWrongQuestionsByUser(userId);
    }
}