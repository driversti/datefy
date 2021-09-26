package dev.seniorjava.datefy.common

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import dev.seniorjava.datefy.exceptions.NoEventFactoryFoundException
import dev.seniorjava.datefy.exceptions.TooManyEventFactoriesFoundException
import spock.lang.Specification
import spock.lang.Subject

class EventFactoryProviderUnitTest extends Specification {

    @Subject
    private EventFactoryProvider factoryProvider

    private Update update

    void setup() {
        update = new Update(message: new Message(text: "/foo"))
    }

    def "should return a factory that matches given selector"() {
        given: "the expected implementation"
        factoryProvider = new EventFactoryProvider([new FooEventFactory()])

        expect:
        factoryProvider.getFactory(update).class == FooEventFactory
    }

    def "should throw a NoEventFactoryFoundException if there is no an implementation for a given selector"() {
        given: "no implementations for the given selector"
        factoryProvider = new EventFactoryProvider([])

        when:
        factoryProvider.getFactory(update)

        then:
        def ex = thrown(NoEventFactoryFoundException)
        ex.message == update.toString()
    }

    def "should throw a TooManyEventFactoriesFoundException if there are more than 1 implementation for a given selector"() {
        given: "two implementations for the given selector"
        factoryProvider = new EventFactoryProvider([new FooEventFactory(), new AnotherFooEventFactory()])

        when:
        factoryProvider.getFactory(update)

        then:
        def ex = thrown(TooManyEventFactoriesFoundException)
        ex.message == "$FooEventFactory.class.simpleName,$AnotherFooEventFactory.class.simpleName"
    }

    private class FooEventFactory implements EventFactory {

        @Override
        Event create(final Update update) {
            return null
        }

        @Override
        boolean canCreate(final Update update) {
            return "/foo" == update.message().text()
        }
    }

    private class AnotherFooEventFactory implements EventFactory {

        @Override
        Event create(final Update update) {
            return null
        }

        @Override
        boolean canCreate(final Update update) {
            return "/foo" == update.message().text()
        }
    }
}
