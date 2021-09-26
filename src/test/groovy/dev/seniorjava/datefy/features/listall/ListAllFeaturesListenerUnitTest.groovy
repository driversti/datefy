package dev.seniorjava.datefy.features.listall

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import spock.lang.Specification
import spock.lang.Subject

class ListAllFeaturesListenerUnitTest extends Specification {

    private TelegramBot bot

    @Subject
    private ListAllFeaturesListener listener

    void setup() {
        bot = Mock()
        listener = new ListAllFeaturesListener(bot)
    }

    def "should send message to the user asking enter dates"() {
        given:
        def event = new ListAllFeaturesEvent(123L)

        when:
        listener.handle(event)

        then:
        1 * bot.execute(_) >> { SendMessage sendMessage ->
            with(sendMessage.parameters) {
                chat_id == 123L
                text == "Select what you want to do:"
                reply_markup.class == InlineKeyboardMarkup
            }
        }
    }
}
