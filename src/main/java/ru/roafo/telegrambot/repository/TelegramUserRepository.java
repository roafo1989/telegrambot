package ru.roafo.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.roafo.telegrambot.domain.TelegramUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {

    List<TelegramUser> findAllByActiveTrue();
    Optional<TelegramUser> findByChatId(String chatId);
    long countAllByActiveTrue();
}
