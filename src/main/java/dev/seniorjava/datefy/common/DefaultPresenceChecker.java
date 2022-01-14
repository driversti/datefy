package dev.seniorjava.datefy.common;

import static java.util.Objects.nonNull;

import com.pengrad.telegrambot.model.Update;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultPresenceChecker implements PresenceChecker {

  @Override
  public boolean isPresent(final Update update, final String... what) {
    return isDirectCommand(update, what) || isCallbackAnswer(update, what);
  }

  @Override
  public boolean startsWith(final Update update, final String with) {
    return inDirectCommand(update, with) || inCallbackQuery(update, with);
  }

  private boolean isDirectCommand(final Update update, final String... what) {
    return nonNull(update) && nonNull(update.message())
        && containsEqual(update.message().text(), what);
  }

  private boolean isCallbackAnswer(final Update update, final String... what) {
    return nonNull(update) && nonNull(update.callbackQuery())
        && containsEqual(update.callbackQuery().data(), what);
  }

  private boolean containsEqual(final String where, final String... what) {
    return Arrays.stream(what).anyMatch(value -> StringUtils.equalsIgnoreCase(where, value));
  }

  private boolean inDirectCommand(final Update update, final String with) {
    return nonNull(update) && nonNull(update.message())
        && startsWith(update.message().text(), with);
  }

  private boolean inCallbackQuery(final Update update, final String with) {
    return nonNull(update) && nonNull(update.callbackQuery())
        && startsWith(update.callbackQuery().data(), with);
  }

  private boolean startsWith(final String what, final String with) {
    return what.startsWith(with);
  }
}
