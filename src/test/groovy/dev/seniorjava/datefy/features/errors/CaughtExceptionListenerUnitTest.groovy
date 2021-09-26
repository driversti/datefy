package dev.seniorjava.datefy.features.errors

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove
import com.pengrad.telegrambot.request.SendMessage
import spock.lang.Specification
import spock.lang.Subject

class CaughtExceptionListenerUnitTest extends Specification {

    private TelegramBot bot
    private final Long developerId = 1234567L

    @Subject
    private CaughtExceptionListener listener

    void setup() {
        bot = Mock()
        listener = new CaughtExceptionListener(bot, developerId)
    }

    def "should notify the developer when an exception was caught and user's chat id is unknown"() {
        given:
        def exception = new RuntimeException("foo message")
        def event = new CaughtExceptionEvent(null, exception)

        when:
        listener.handle(event)

        then:
        1 * bot.execute(_) >> { SendMessage sendMessage ->
            with(sendMessage.parameters) {
                chat_id == 1234567L
                text == "Got $exception.class.simpleName: $exception.message"
            }
        }
    }

    def "should notify the developer and the user when an exception was caught"() {
        given:
        def exception = new RuntimeException("foo message")
        def event = new CaughtExceptionEvent(555L, exception)

        when:
        listener.handle(event)

        then:
        1 * bot.execute(_) >> { SendMessage sendMessage ->
            with(sendMessage.parameters) {
                chat_id == 1234567L
                text == "Got $exception.class.simpleName: $exception.message"
            }
        }
        1 * bot.execute(_) >> { SendMessage sendMessage ->
            with(sendMessage.parameters) {
                chat_id == 555L
                text == "Ooops, you're a lucky man and caught a bug!\n" +
                        "But don't worry, I've notified the developer already.\n" +
                        "Press /features to continue"
                reply_markup.class == ReplyKeyboardRemove
            }
        }
    }
}
