package com.example.mapper;

import com.example.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Select("SELECT * FROM question ORDER BY id DESC")
    List<Question> findAll();

    @Select("SELECT * FROM question WHERE id = #{id}")
    Question findById(Integer id);

    @Insert("INSERT INTO question(title, option_a, option_b, option_c, option_d, correct_answer) " +
            "VALUES(#{title}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, #{correctAnswer})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Question question);

    @Update("UPDATE question SET title = #{title}, option_a = #{optionA}, option_b = #{optionB}, " +
            "option_c = #{optionC}, option_d = #{optionD}, correct_answer = #{correctAnswer} " +
            "WHERE id = #{id}")
    int update(Question question);

    @Delete("DELETE FROM question WHERE id = #{id}")
    int delete(Integer id);
}