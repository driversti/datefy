package dev.seniorjava.datefy.features.daysbetween.response;

import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.common.Event;
import dev.seniorjava.datefy.common.EventFactory;
import dev.seniorjava.datefy.features.daysbetween.request.DaysBetweenListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DaysBetweenResponseEventFactory implements EventFactory {

  @Override
  public Event create(Update update) {
    final Long chatId = update.message().chat().id();
    final String userResponse = update.message().text();
    return new DaysBetweenResponseEvent(chatId, userResponse);
  }

  @Override
  public boolean canCreate(Update update) {
    if (update == null || update.message() == null || update.message().replyToMessage() == null) {
      return false;
    }
    final String userReply = update.message().replyToMessage().text();
    return DaysBetweenListener.HEADER.equals(userReply)
        || DaysBetweenResponseListener.WRONG_DATES_PROVIDED.equals(userReply);
  }
}
