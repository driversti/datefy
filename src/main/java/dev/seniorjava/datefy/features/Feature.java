package dev.seniorjava.datefy.features;

import com.pengrad.telegrambot.model.Update;

@FunctionalInterface
public interface Feature {

  Result handle(final Update update);

}
