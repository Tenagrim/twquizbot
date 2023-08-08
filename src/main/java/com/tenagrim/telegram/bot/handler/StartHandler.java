package com.tenagrim.telegram.bot.handler;

import com.tenagrim.telegram.bot.State;
import com.tenagrim.telegram.model.User;
import com.tenagrim.telegram.repository.UserRepository;
import com.tenagrim.telegram.service.QuestionService;
import com.tenagrim.telegram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tenagrim.telegram.util.TelegramUtil.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {
    private final QuestionService questionService;
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<SendMessage> messages = new ArrayList<>();

        if (user.getNickname() == null) {
            messages.add(createMessageTemplate(user,"Привет! Введите свой никнейм:"));
            user.setUserStateId(State.AUTHORIZATION.getId());
        } else {
            userService.updateSession(user);
            messages.add(createMessageTemplate(user, String.format("Начинаем викторину, %s!", user.getNickname())));
            messages.add(questionService.initQuestion(user));
            user.setUserStateId(State.AUTHORIZED.getId());
        }
        userService.saveUser(user);

        return List.copyOf(messages);
    }

    @Override
    public State operatedBotState() {
        return State.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
