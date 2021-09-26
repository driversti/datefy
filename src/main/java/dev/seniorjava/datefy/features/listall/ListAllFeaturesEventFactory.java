package dev.seniorjava.datefy.features.listall;

import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.common.Event;
import dev.seniorjava.datefy.common.EventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ListAllFeaturesEventFactory implements EventFactory {

  private static final String SELECTOR = "/features";

  @Override
  public Event create(Update update) {
    return new ListAllFeaturesEvent(update.message().chat().id());
  }

  @Override
  public boolean canCreate(Update update) {
    if (update == null || update.message() == null) {
      return false;
    }
    return SELECTOR.equals(update.message().text());
  }
}
