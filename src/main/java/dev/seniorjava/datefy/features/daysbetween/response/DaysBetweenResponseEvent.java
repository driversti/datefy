package dev.seniorjava.datefy.features.daysbetween.response;

import dev.seniorjava.datefy.common.Event;

public record DaysBetweenResponseEvent(Long chatId, String userResponse) implements Event {

}
