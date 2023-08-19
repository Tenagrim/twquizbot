package com.tenagrim.telegram.controller;

import com.tenagrim.telegram.bot.Bot;
import com.tenagrim.telegram.dto.Message;
import com.tenagrim.telegram.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final MessageService messageService;

    private final Bot bot;

    @PostMapping("/broadcast")
    public void broadcast(@RequestBody Message message){
        List<SendMessage> messagesToSend = messageService.getBroadcastMessages(message.getText());

        messagesToSend.forEach(bot::executeWithExceptionCheck);
    }

}
