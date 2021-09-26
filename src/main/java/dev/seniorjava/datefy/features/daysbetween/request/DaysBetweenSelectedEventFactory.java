package dev.seniorjava.datefy.features.daysbetween.request;

import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.common.Event;
import dev.seniorjava.datefy.common.EventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DaysBetweenSelectedEventFactory implements EventFactory {

  public static final String SELECTOR = "/daysbetween";

  @Override
  public Event create(Update update) {
    final Long chatId = update.callbackQuery().message().chat().id();
    return new DaysBetweenSelectedEvent(chatId);
  }

  @Override
  public boolean canCreate(final Update update) {
    if (update == null || update.callbackQuery() == null) {
      return false;
    }
    return SELECTOR.equals(update.callbackQuery().data());
  }
}
