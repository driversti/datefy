package dev.seniorjava.datefy.features.errors;

import com.pengrad.telegrambot.model.Update;

public record CaughtExceptionEvent(Update update, Exception exception) {

}
