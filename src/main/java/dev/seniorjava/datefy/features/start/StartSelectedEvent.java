package dev.seniorjava.datefy.features.start;

import com.pengrad.telegrambot.model.Update;
import dev.seniorjava.datefy.common.Event;

public record StartSelectedEvent(Long chatId, Update update) implements Event {

}
