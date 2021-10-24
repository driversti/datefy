package dev.seniorjava.datefy;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.features.FeatureManager;
import dev.seniorjava.datefy.features.errors.CaughtExceptionEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class DefaultUpdatesListener implements UpdatesListener {

  private final FeatureManager featureManager;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public int process(final List<Update> updates) {
    for (Update update : updates) {
    int lastSuccessfullyProcessedUpdateId = update.updateId();
      try {
        featureManager.processUpdate(update);
      } catch (Exception exception) {
        // todo write as json
        log.error("{}", update, exception);
        notifyDeveloperAboutException(update, exception);
        return lastSuccessfullyProcessedUpdateId;
      }
    }
    return UpdatesListener.CONFIRMED_UPDATES_ALL;
  }

  private void notifyDeveloperAboutException(final Update update, final Exception exception) {
    // todo implement
    eventPublisher.publishEvent(new CaughtExceptionEvent(update, exception));
  }
}
