package com.tenagrim.telegram.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "SESSION_ANSWER")
@NoArgsConstructor
public class SessionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "SESSION_ID")
    Long sessionId;

    @Column(name = "ANSWER_ID")
    Long answerId;

    public SessionAnswer(Long sessionId, Long answerId) {
        this.sessionId = sessionId;
        this.answerId = answerId;
    }
}
