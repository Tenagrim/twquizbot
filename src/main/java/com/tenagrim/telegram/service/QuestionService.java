package com.tenagrim.telegram.service;

import com.tenagrim.telegram.model.Answer;
import com.tenagrim.telegram.model.Question;
import com.tenagrim.telegram.model.SessionAnswer;
import com.tenagrim.telegram.model.User;
import com.tenagrim.telegram.repository.QuestionRepository;
import com.tenagrim.telegram.repository.SessionAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final SessionAnswerRepository sessionAnswerRepository;
    private final MessageService messageService;
    private final UserService userService;


    public SendMessage initQuestion(User user) {
        Question randomQuestion = questionRepository.getRandomQuestion().get();
        userService.updateLastQuestion(user, randomQuestion);
        return messageService.getMessage(user, randomQuestion);
    }

    public Optional<SendMessage> getNextQuestion(User user) {
        List<Question> unanswered = getUnanswered(user);
        if (unanswered.isEmpty()) {
            return Optional.empty();
        }
        Question question = unanswered.iterator().next();
        userService.updateLastQuestion(user, question); // TODO more randomness
        return Optional.of(messageService.getMessage(user, question));
    }

    public SendMessage finishMessage(User user, Date finishDate) {
        SendMessage message = new SendMessage();
        message.setChatId(user.getChatId());
        Long rightAnswers = sessionAnswerRepository.countRightAnswers(user.getActiveSession().getId());
        Long timeDiff = (finishDate.getTime()-user.getActiveSession().getCreateDate().getTime())/1000;
        message.setText(String.format("Викторина окончена!\n" +
                "Никнейм: %s\n" +
                "Время выполнения: %d сек\n" +
                "Количество правильных ответов: %d", user.getNickname(), timeDiff, rightAnswers));
        message.enableMarkdown(false);
        return message;
    }

    public void makeAnswer(User user, Answer answer) {
        sessionAnswerRepository.save(new SessionAnswer(user.getActiveSession().getId(), answer.getId()));
    }


    private List<Question> getUnanswered(User user) {
        return questionRepository.getUnansweredQuestions(user.getId());
    }

}
