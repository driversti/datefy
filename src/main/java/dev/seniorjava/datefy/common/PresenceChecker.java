package dev.seniorjava.datefy.common;

import com.pengrad.telegrambot.model.Update;

public interface PresenceChecker {

  boolean isPresent(final Update update, final String... what);

  boolean startsWith(final Update update, final String with);
}
