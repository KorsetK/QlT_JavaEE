package com.example.mapper;

import com.example.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WrongBookMapper {

    @Insert("INSERT IGNORE INTO wrong_book(user_id, question_id) VALUES(#{userId}, #{questionId})")
    int insert(@Param("userId") Integer userId, @Param("questionId") Integer questionId);

    @Delete("DELETE FROM wrong_book WHERE user_id = #{userId} AND question_id = #{questionId}")
    int delete(@Param("userId") Integer userId, @Param("questionId") Integer questionId);

    @Select("SELECT q.* FROM question q " +
            "JOIN wrong_book wb ON q.id = wb.question_id " +
            "WHERE wb.user_id = #{userId} " +
            "ORDER BY wb.created_at DESC")
    List<Question> findWrongQuestionsByUser(Integer userId);
}