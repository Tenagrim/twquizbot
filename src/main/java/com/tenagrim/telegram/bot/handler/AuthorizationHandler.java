package com.tenagrim.telegram.bot.handler;

import com.tenagrim.telegram.bot.State;
import com.tenagrim.telegram.model.User;
import com.tenagrim.telegram.service.QuestionService;
import com.tenagrim.telegram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

import static com.tenagrim.telegram.util.TelegramUtil.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class AuthorizationHandler implements Handler {
    private final UserService userService;
    private final QuestionService questionService;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z]{1,50}");

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<SendMessage> messages = new ArrayList<>();
        if (isValidUsername(message)) {
            user.setNickname(message.toLowerCase());
            userService.updateSession(user);
            messages.add(createMessageTemplate(user,String.format("Начинаем викторину, %s!", message)));
            messages.add(questionService.initQuestion(user));
            user.setUserStateId(State.AUTHORIZED.getId());
            userService.saveUser(user);
        } else {
            // TODO больше прикольных штук;
            messages.add(createMessageTemplate(user,"Чет не то, попробуй ввести свой никнейм"));

        }
        return List.copyOf(messages);
    }

    private boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }


    @Override
    public State operatedBotState() {
        return State.AUTHORIZATION;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
