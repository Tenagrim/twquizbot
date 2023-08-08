package com.tenagrim.telegram.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Answer {
    @Id
    Long id;
    @Column(name = "TEXT")
    String text;
    @Column(name = "RIGHT_ANSWER")
    Boolean rightAnswer;
}
