package gg.bayes.challenge.repo.event;

import gg.bayes.challenge.domain.event.HitEvent;
import gg.bayes.challenge.domain.projection.DamageCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HitEventRepository extends EventRepository<HitEvent> {

    @Query("SELECT c.target AS target, COUNT(c.id) AS damageInstances, SUM(c.damage) as totalDamage FROM hits AS c " +
            "WHERE c.match.id = :matchId AND c.heroName = :heroName GROUP BY target")
    List<DamageCount> countDamageInMatchByHero(long matchId, String heroName);
}
