package com.tenagrim.telegram.util;

import com.tenagrim.telegram.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramUtil {
    public static SendMessage createMessageTemplate(User user) {
        return createMessageTemplate(String.valueOf(user.getChatId()));
    }

    public static SendMessage createMessageTemplate(User user, String text) {
        SendMessage messageTemplate = createMessageTemplate(user);
        messageTemplate.setText(text);
        return messageTemplate;
    }

    // Создаем шаблон SendMessage с включенным Markdown
    public static SendMessage createMessageTemplate(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableMarkdown(true);
        return message;
    }

    // Создаем кнопку
    public static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(command);
        return button;
    }
}
