package dev.seniorjava.datefy.common;

import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.exceptions.NoEventFactoryFoundException;
import dev.seniorjava.datefy.exceptions.TooManyEventFactoriesFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventFactoryProvider {

  private static final int FIRST = 0;

  private final Collection<EventFactory> eventFactories;

  public EventFactory getFactory(final Update update) {
    final List<EventFactory> foundFactories = eventFactories.stream()
        .filter(it -> it.canCreate(update))
        .collect(Collectors.toList());
    if (foundFactories.isEmpty()) {
      throw new NoEventFactoryFoundException(update.toString());
    }
    if (foundFactories.size() > 1) {
      tooManyFactoriesFoundException(foundFactories);
    }
    return foundFactories.get(FIRST);
  }

  private static void tooManyFactoriesFoundException(List<EventFactory> foundFactories) {
    final String factoriesNames = foundFactories.stream()
        .map(it -> it.getClass().getSimpleName())
        .collect(Collectors.joining(","));
    throw new TooManyEventFactoriesFoundException(factoriesNames);
  }
}
