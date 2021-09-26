package dev.seniorjava.datefy

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import spock.lang.Specification
import spock.lang.Subject

class BotRunnerUnitTest extends Specification {

    private TelegramBot bot
    private UpdatesListener updatesListener

    @Subject
    private BotRunner runner

    void setup() {
        bot = Mock()
        updatesListener = Mock()

        runner = new BotRunner(bot, updatesListener)
    }

    def "should set the given UpdatesListener to the TelegramBot"() {
        when:
        runner.run()

        then:
        1 * bot.setUpdatesListener(updatesListener)
    }
}
