package com.tenagrim.telegram.service;

import com.tenagrim.telegram.model.Question;
import com.tenagrim.telegram.model.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.stream.Collectors;

@Service
public class MessageService {
    public SendMessage getMessage(User user, Question question){
        SendMessage message= new SendMessage();
        message.setChatId(user.getChatId());
        message.setText(question.getText());
        message.enableMarkdown(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(question.getAnswers().stream()
                .map(a-> {
                    KeyboardRow keyboardButtons = new KeyboardRow();
                    keyboardButtons.add(a.getText());
                    return keyboardButtons;
                })
                .collect(Collectors.toList()));
        message.setReplyMarkup(replyKeyboardMarkup);
        return message;
    }
}
