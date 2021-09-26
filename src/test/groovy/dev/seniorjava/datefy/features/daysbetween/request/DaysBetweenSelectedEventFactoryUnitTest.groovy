package dev.seniorjava.datefy.features.daysbetween.request

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import spock.lang.Specification
import spock.lang.Subject

class DaysBetweenSelectedEventFactoryUnitTest extends Specification {

    @Subject
    private factory = new DaysBetweenSelectedEventFactory()

    def "should create an event"() {
        given:
        final Long givenChatId = 123
        final String data = "/daysbetween"
        def givenUpdate = new Update(callback_query: new CallbackQuery(data: data,
                message: new Message(chat: new Chat(id: givenChatId))))

        when:
        def event = factory.create(givenUpdate)

        then:
        event instanceof DaysBetweenSelectedEvent
        with(event as DaysBetweenSelectedEvent) {
            chatId() == givenChatId
        }
    }

    def "should check whether an event can be created"() {
        expect:
        factory.canCreate(update) == canCreate

        where:
        update                                                              || canCreate
        null                                                                || false
        new Update()                                                        || false
        new Update(callback_query: new CallbackQuery(data: "/daysbetween")) || true
        new Update(callback_query: new CallbackQuery(data: "/abcde"))       || false
    }
}
