package com.tenagrim.telegram.bot.handler;

import com.tenagrim.telegram.bot.State;
import com.tenagrim.telegram.model.Answer;
import com.tenagrim.telegram.model.User;
import com.tenagrim.telegram.service.QuestionService;
import com.tenagrim.telegram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.tenagrim.telegram.util.TelegramUtil.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class AuthorizedHandler implements Handler {
    private final QuestionService questionService;
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<SendMessage> messages = new ArrayList<>();
        Optional<Answer> answer = user.getActiveSession().getLastQuestion().getAnswers().stream()
                .filter(a -> a.getText().equals(message))
                .findFirst();
        if (answer.isPresent()) {
            questionService.makeAnswer(user, answer.get());
            Optional<SendMessage> nextQuestion = questionService.getNextQuestion(user);
            if (nextQuestion.isPresent()) {
                messages.add(nextQuestion.get());
            } else {
                user.setUserStateId(State.START.getId());
                Date finishDate;
                if (user.getActiveSession().getFinishDate() == null) {
                    finishDate = new Date();
                    userService.finishSession(user, finishDate);
                } else {
                    finishDate = user.getActiveSession().getFinishDate();
                }
                messages.add(questionService.finishMessage(user, finishDate));
                userService.saveUser(user);
            }

        } else {
            messages.add(createMessageTemplate(user,"Чет не то, попробуй ввести ответ из списка"));
        }
        return List.copyOf(messages);
    }

    @Override
    public State operatedBotState() {
        return State.AUTHORIZED;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return null;
    }
}
