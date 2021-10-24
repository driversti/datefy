package dev.seniorjava.datefy.common;

import com.pengrad.telegrambot.model.Update;

public interface ChatIdExtractor {

  Long chatId(final Update update);
}
