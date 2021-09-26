package dev.seniorjava.datefy.features.start

import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import spock.lang.Specification
import spock.lang.Subject

class StartSelectedEventFactoryUnitTest extends Specification {

    @Subject
    private factory = new StartSelectedEventFactory()

    def "should create an event"() {
        given:
        final Long givenChatId = 123
        final String text = "/start"
        def givenUpdate = new Update(message: new Message(text: text, chat: new Chat(id: givenChatId)))

        when:
        def event = factory.create(givenUpdate)

        then:
        event instanceof StartSelectedEvent
        with(event as StartSelectedEvent) {
            chatId() == givenChatId
            update() == givenUpdate
        }
    }

    def "should check whether an event can be created"() {
        expect:
        factory.canCreate(update) == canCreate

        where:
        update                                           || canCreate
        null                                             || false
        new Update()                                     || false
        new Update(message: new Message(text: "/start")) || true
        new Update(message: new Message(text: "/abcde")) || false
    }
}
