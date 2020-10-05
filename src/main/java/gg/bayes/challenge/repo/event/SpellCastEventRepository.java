package gg.bayes.challenge.repo.event;

import gg.bayes.challenge.domain.event.SpellCastEvent;
import gg.bayes.challenge.domain.projection.SpellCastsCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpellCastEventRepository extends EventRepository<SpellCastEvent> {


    @Query("SELECT c.spellName AS spell, COUNT(c.id) AS casts FROM spell_casts AS c " +
            "WHERE c.match.id = :matchId AND c.heroName = :heroName GROUP BY c.spellName")
    List<SpellCastsCount> countSpellCastInMatchByHeroName(long matchId, String heroName);
}
