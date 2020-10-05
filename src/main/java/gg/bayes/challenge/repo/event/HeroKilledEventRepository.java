package gg.bayes.challenge.repo.event;

import gg.bayes.challenge.domain.event.HeroKilledEvent;
import gg.bayes.challenge.domain.projection.HeroKillsCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroKilledEventRepository extends EventRepository<HeroKilledEvent> {

    @Query("SELECT c.killedBy AS hero, COUNT(c.id) AS kills FROM heroes_killings AS c WHERE c.match.id = :matchId GROUP BY c.killedBy")
    List<HeroKillsCount> countKillsInMatch(long matchId);
}
