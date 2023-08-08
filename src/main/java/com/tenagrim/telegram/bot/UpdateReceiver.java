package com.tenagrim.telegram.bot;

import com.tenagrim.telegram.bot.handler.Handler;
import com.tenagrim.telegram.model.User;
import com.tenagrim.telegram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateReceiver {
    private final List<Handler> handlers;
    private final UserRepository userRepository;

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        // try-catch, чтобы при несуществующей команде просто возвращать пустой список
        try {
            // Проверяем, если Update - сообщение с текстом
            if (isMessageWithText(update)) {
                // Получаем Message из Update
                final Message message = update.getMessage();
                // Получаем айди чата с пользователем
                final Long chatId = message.getFrom().getId();
                //update.getMessage().get
                // Просим у репозитория пользователя. Если такого пользователя нет - создаем нового и возвращаем его.
                // Как раз на случай нового пользователя мы и сделали конструктор с одним параметром в классе User
                final User user = userRepository.findByChatId(chatId.toString())
                        .orElseGet(() -> userRepository.save(new User(chatId.toString())));
                // Ищем нужный обработчик и возвращаем результат его работы
                return getHandlerByStateId(user.getUserStateId()).handle(user, message.getText());

            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                final Long chatId = callbackQuery.getFrom().getId();
                final User user = userRepository.findByChatId(chatId.toString())
                        .orElseGet(() -> userRepository.save(new User(chatId.toString())));

                return getHandlerByCallBackQuery(callbackQuery.getData()).handle(user, callbackQuery.getData());
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByStateId(Long stateId) {
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().getId().equals(stateId))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }

}
