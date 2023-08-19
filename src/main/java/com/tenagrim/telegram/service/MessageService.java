package com.tenagrim.telegram.service;

import com.tenagrim.telegram.model.Question;
import com.tenagrim.telegram.model.User;
import com.tenagrim.telegram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserService userService;

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

    public SendMessage getMessage(User user, String text){
        SendMessage message= new SendMessage();
        message.setChatId(user.getChatId());
        message.setText(text);
        message.enableMarkdown(false);
        return message;
    }

    public List<SendMessage> getBroadcastMessages(String text){
        List<User> users = userService.getAllUsers()
                .stream().filter(user -> user.getChatId().equals("805419560")).collect(Collectors.toList()); // debug

        return users.stream().map(user -> getMessage(user,text)).collect(Collectors.toList());
    }
}
