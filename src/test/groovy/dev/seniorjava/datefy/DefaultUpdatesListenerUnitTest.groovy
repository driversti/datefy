package dev.seniorjava.datefy

import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import dev.seniorjava.datefy.common.Event
import dev.seniorjava.datefy.common.EventFactory
import dev.seniorjava.datefy.common.EventFactoryProvider
import dev.seniorjava.datefy.features.errors.CaughtExceptionEvent
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification
import spock.lang.Subject

class DefaultUpdatesListenerUnitTest extends Specification {

    private EventFactoryProvider factoryProvider
    private ApplicationEventPublisher eventPublisher

    @Subject
    private DefaultUpdatesListener updatesListener

    void setup() {
        factoryProvider = Mock()
        eventPublisher = Mock()
        updatesListener = new DefaultUpdatesListener(factoryProvider, eventPublisher)
    }

    def "should process all updates and return an appropriate confirmation"() {
        given: "a list of updates"
        def updates = [new Update()]

        and: "set up the behavior for mocks"
        def eventFactory = Mock(EventFactory)
        factoryProvider.getFactory(_ as Update) >> eventFactory
        eventFactory.create(_ as Update) >> Mock(Event)

        when:
        def response = updatesListener.process(updates)

        then:
        1 * eventPublisher.publishEvent(_ as Event)
        response == UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    def "should publish a CaughtExceptionEvent if any exception has been caught during processing updates"() {
        given: "a list of updates"
        def userChatId = 12345L
        def updates = [new Update(message: new Message(chat: new Chat(id: userChatId)))]

        and: "set up the behavior for mocks"
        def eventFactory = Mock(EventFactory)
        factoryProvider.getFactory(_ as Update) >> eventFactory
        eventFactory.create(_ as Update) >> Mock(Event)
        1 * eventPublisher.publishEvent(_ as Event) >> { throw new RuntimeException("foo message") }

        when:
        updatesListener.process(updates)

        then:
        1 * eventPublisher.publishEvent(_ as CaughtExceptionEvent) >> { args ->
            def exceptionEvent = args[0] as CaughtExceptionEvent
            exceptionEvent.userChatId() == userChatId
            with(exceptionEvent.exception()) {
                it.class == RuntimeException
                it.message == "foo message"
            }
        }
    }
}
