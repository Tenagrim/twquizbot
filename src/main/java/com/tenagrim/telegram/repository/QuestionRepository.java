package com.tenagrim.telegram.repository;

import com.tenagrim.telegram.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query(value = "select distinct q.ID, q.TEXT\n" +
            "from QUESTION q\n" +
            "    join ANSWER A on q.ID = A.QUESTION_ID\n" +
            "    left join (select distinct QUESTION_ID\n" +
            "               from Q_SESSION s\n" +
            "                        join Q_USER u on u.ACTIVE_SESSION_ID = s.ID\n" +
            "                        join SESSION_ANSWER se on se.SESSION_ID = s.ID\n" +
            "                        join ANSWER A on se.ANSWER_ID = A.ID\n" +
            "                    where u.ID = :user_id\n" +
            "                ) user_answers on user_answers.question_id = q.ID\n" +
            "    where user_answers.QUESTION_ID is null\n" +
            ";", nativeQuery = true)
    List<Question> getUnansweredQuestions(@Param("user_id") Integer userId);

    @Query(value = "select q.ID, q.TEXT\n" +
            "from QUESTION q\n" +
            "order by random()\n" +
            "limit 1", nativeQuery = true)
    Optional<Question> getRandomQuestion();

}
