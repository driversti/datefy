package dev.seniorjava.datefy.features.daysbetween.response


import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import dev.seniorjava.datefy.features.daysbetween.request.DaysBetweenListener
import spock.lang.Specification
import spock.lang.Subject

class DaysBetweenResponseEventFactoryUnitTest extends Specification {

    @Subject
    private factory = new DaysBetweenResponseEventFactory()

    def "should create an event"() {
        given:
        final Long givenChatId = 123
        final String givenText = "12.12.2000\n23.05.2021"
        def givenUpdate = new Update(message: new Message(chat: new Chat(id: givenChatId), text: givenText))

        when:
        def event = factory.create(givenUpdate)

        then:
        event instanceof DaysBetweenResponseEvent
        with(event as DaysBetweenResponseEvent) {
            chatId() == givenChatId
            userResponse() == givenText
        }
    }

    def "should check whether an event can be created"() {
        expect:
        factory.canCreate(update) == canCreate

        where:
        update                                                                    || canCreate
        null                                                                      || false
        new Update()                                                              || false
        new Update(message: new Message())                                        || false
        new Update(message: new Message(
                reply_to_message: new Message()))                                 || false
        new Update(message: new Message(
                reply_to_message: new Message(text: DaysBetweenListener.HEADER))) || true
    }
}
