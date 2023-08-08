package com.tenagrim.telegram.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Q_SESSION")
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToOne
    private Question lastQuestion;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "FINISH_DATE")
    private Date finishDate;
}
