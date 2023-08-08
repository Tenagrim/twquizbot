package com.tenagrim.telegram.model;

import lombok.Getter;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Question {
    @Id
    Integer id;

    @Column(name = "TEXT")
    String text;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "QUESTION_ID", referencedColumnName = "ID")
    List<Answer> answers;
}
