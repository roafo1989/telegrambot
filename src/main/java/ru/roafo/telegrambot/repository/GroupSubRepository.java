package ru.roafo.telegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.roafo.telegrambot.domain.GroupSub;

/**
* {@link Repository} for {@link GroupSub} entity.
*/
@Repository
public interface GroupSubRepository extends JpaRepository<GroupSub, Integer> {
}