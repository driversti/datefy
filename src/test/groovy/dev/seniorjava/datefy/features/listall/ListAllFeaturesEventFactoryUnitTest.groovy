package dev.seniorjava.datefy.features.listall

import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import spock.lang.Specification
import spock.lang.Subject

class ListAllFeaturesEventFactoryUnitTest extends Specification {

    @Subject
    private factory = new ListAllFeaturesEventFactory()

    def "should create an event"() {
        given:
        final Long givenChatId = 123
        final String text = "/features"
        def givenUpdate = new Update(message: new Message(text: text, chat: new Chat(id: givenChatId)))

        when:
        def event = factory.create(givenUpdate)

        then:
        event instanceof ListAllFeaturesEvent
        with(event as ListAllFeaturesEvent) {
            chatId() == givenChatId
        }
    }

    def "should check whether an event can be created"() {
        expect:
        factory.canCreate(update) == canCreate

        where:
        update                                              || canCreate
        null                                                || false
        new Update()                                        || false
        new Update(message: new Message(text: "/features")) || true
        new Update(message: new Message(text: "/abcde"))    || false
    }
}
