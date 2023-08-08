package com.tenagrim.telegram.repository;

import com.tenagrim.telegram.model.SessionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionAnswerRepository extends JpaRepository<SessionAnswer, Integer> {
    @Query(value = "select count(*)\n" +
            "from SESSION_ANSWER se \n" +
            "join ANSWER A on se.ANSWER_ID = A.ID\n" +
            "where a.RIGHT_ANSWER = true\n" +
            "and SESSION_ID = :session_id", nativeQuery = true)
    Long countRightAnswers(@Param("session_id") Long sessionId);
}
