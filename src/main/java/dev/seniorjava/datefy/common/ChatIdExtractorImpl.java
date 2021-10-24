package dev.seniorjava.datefy.common;

import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatIdExtractorImpl implements ChatIdExtractor {

  @Override
  public Long chatId(final Update update) {
    return update.message() != null ?
        update.message().chat().id() :
        update.callbackQuery().message().chat().id();
  }
}
