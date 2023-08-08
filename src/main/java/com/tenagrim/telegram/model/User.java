package com.tenagrim.telegram.model;

import com.tenagrim.telegram.bot.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Q_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractBaseEntity {
    @Column(name = "CHAT_ID", unique = true, nullable = false)
    @NotNull
    private String chatId;

    @Column(name = "EXTERNAL_ID")
    @NotNull
    private int externalId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "STATE_ID")
    private Long userStateId;

    @OneToOne
    @JoinColumn(name = "ACTIVE_SESSION_ID", referencedColumnName = "ID")
    private Session activeSession;

    public User(String chatId) {
        this.chatId = chatId;
        this.userStateId = State.START.getId();
    }
}
