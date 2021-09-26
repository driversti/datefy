package dev.seniorjava.datefy.features.start;

import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.common.Event;
import dev.seniorjava.datefy.common.EventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartSelectedEventFactory implements EventFactory {

  private static final String SELECTOR = "/start";

  @Override
  public Event create(final Update update) {
    final Long chatId = update.message().chat().id();
    return new StartSelectedEvent(chatId, update);
  }

  @Override
  public boolean canCreate(final Update update) {
    if (update == null || update.message() == null) {
      return false;
    }
    return SELECTOR.equals(update.message().text());
  }
}
