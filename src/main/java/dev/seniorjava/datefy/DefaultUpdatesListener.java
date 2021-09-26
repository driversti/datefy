package dev.seniorjava.datefy;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.common.Event;
import dev.seniorjava.datefy.common.EventFactoryProvider;
import dev.seniorjava.datefy.features.errors.CaughtExceptionEvent;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultUpdatesListener implements UpdatesListener {

  private final EventFactoryProvider eventFactoryProvider;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public int process(final List<Update> updates) {
    updates.forEach(this::processUpdate);
    return UpdatesListener.CONFIRMED_UPDATES_ALL;
  }

  private void processUpdate(final Update update) {
    try {
      publishRelevantEvent(update);
    } catch (Exception exception) {
      notifyDeveloperAboutException(exception, update);
    }
  }

  private void publishRelevantEvent(final Update update) {
    log.debug("{}", update);
    final Event event = eventFactoryProvider.getFactory(update).create(update);
    eventPublisher.publishEvent(event);
  }

  private void notifyDeveloperAboutException(final Exception e, final Update update) {
    log.error("Ooops", e);
    Long userChatId = findUserChatId(update).orElse(null);
    eventPublisher.publishEvent(new CaughtExceptionEvent(userChatId, e));
  }

  private Optional<Long> findUserChatId(Update update) {
    if (update.message() != null) {
      return Optional.of(update.message().chat().id());
    } else if (update.callbackQuery() != null) {
      return Optional.of(update.callbackQuery().message().chat().id());
    }
    return Optional.empty();
  }
}
