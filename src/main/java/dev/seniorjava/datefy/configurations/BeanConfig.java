package dev.seniorjava.datefy.configurations;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  TelegramBot bot(@Value("${app.bot.token}") String token) {
    return new TelegramBot(token);
  }

}
