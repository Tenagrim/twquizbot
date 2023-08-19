package com.tenagrim.telegram.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
public class Message {
    @NotNull
    private String text;
}
