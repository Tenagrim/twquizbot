package com.tenagrim.telegram.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_STATE")
@Getter
public class UserState {
    @Id
    private Long id;

    @Column(name = "SYSNAME")
    private String sysname;

    public UserState() {
        this.id = 0L;
        this.sysname = "START";
    }
}
