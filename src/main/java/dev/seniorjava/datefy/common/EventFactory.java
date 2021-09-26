package dev.seniorjava.datefy.common;

import com.pengrad.telegrambot.model.Update;

public interface EventFactory {

  Event create(final Update update);

  boolean canCreate(final Update update);
}
