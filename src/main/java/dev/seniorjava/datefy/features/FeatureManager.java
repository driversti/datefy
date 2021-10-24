package dev.seniorjava.datefy.features;

import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.features.errors.UpdateNotHandledEvent;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeatureManager {

  private final Collection<Feature> features;
  private final ApplicationEventPublisher eventPublisher;

  public void processUpdate(final Update update) {
    final Map<Result, Long> results = features.stream()
        .map(feature -> feature.handle(update))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    final boolean updateWasNotHandled = results.get(Result.HANDLED) == null;
    if (updateWasNotHandled) {
      eventPublisher.publishEvent(new UpdateNotHandledEvent(update));
    }
  }
}
