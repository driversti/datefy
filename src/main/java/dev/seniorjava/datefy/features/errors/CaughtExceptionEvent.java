package dev.seniorjava.datefy.features.errors;

import dev.seniorjava.datefy.common.Event;

public record CaughtExceptionEvent(Long userChatId, Exception exception) implements Event {

}
