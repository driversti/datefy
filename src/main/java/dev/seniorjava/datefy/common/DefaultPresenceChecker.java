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
  public boolean isPresent(final Update update, final String... values) {
    return isDirectCommand(update, values) || isCallbackAnswer(update, values);
  }

  private boolean isDirectCommand(final Update update, final String... values) {
    return nonNull(update) && nonNull(update.message())
        && isPresent(update.message().text(), values);
  }

  private boolean isCallbackAnswer(final Update update, final String... values) {
    return nonNull(update) && nonNull(update.callbackQuery())
        && isPresent(update.callbackQuery().data(), values);
  }

  private boolean isPresent(final String where, final String... what) {
    return Arrays.stream(what).anyMatch(value -> StringUtils.equalsIgnoreCase(where, value));
  }
}
