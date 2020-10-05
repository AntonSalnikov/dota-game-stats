package gg.bayes.challenge.repo.event;

import gg.bayes.challenge.domain.event.Event;
import gg.bayes.challenge.domain.event.HeroKilledEvent;
import gg.bayes.challenge.domain.event.HitEvent;
import gg.bayes.challenge.domain.event.PurchasedItemEvent;
import gg.bayes.challenge.domain.event.SpellCastEvent;
import gg.bayes.challenge.domain.projection.BuyItemProjection;
import gg.bayes.challenge.domain.projection.DamageCount;
import gg.bayes.challenge.domain.projection.HeroKillsCount;
import gg.bayes.challenge.domain.projection.SpellCastsCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EventRepositoryFacade {

    private final HeroKilledEventRepository heroKilledEventRepository;
    private final HitEventRepository hitEventRepository;
    private final PurchasedItemEventRepository purchasedItemEventRepository;
    private final SpellCastEventRepository spellCastEventRepository;

    public EventRepositoryFacade(HeroKilledEventRepository heroKilledEventRepository,
                                 HitEventRepository hitEventRepository,
                                 PurchasedItemEventRepository purchasedItemEventRepository,
                                 SpellCastEventRepository spellCastEventRepository) {
        this.heroKilledEventRepository = heroKilledEventRepository;
        this.hitEventRepository = hitEventRepository;
        this.purchasedItemEventRepository = purchasedItemEventRepository;
        this.spellCastEventRepository = spellCastEventRepository;
    }

    public <T extends Event> void save(T event) {
        log.debug("Resolving repository for class {}", event.getClass());

        if(event instanceof HeroKilledEvent) {
            heroKilledEventRepository.save((HeroKilledEvent) event);
            return;
        }

        if(event instanceof HitEvent) {
            hitEventRepository.save((HitEvent) event);
            return;
        }

        if(event instanceof PurchasedItemEvent) {
            purchasedItemEventRepository.save((PurchasedItemEvent) event);
            return;
        }

        if(event instanceof SpellCastEvent) {
            spellCastEventRepository.save((SpellCastEvent) event);
            return;
        }

        log.warn("No repo was defined for {}", event);
    }

    public List<HeroKillsCount> countKillsInMatch(long matchId) {
        return heroKilledEventRepository.countKillsInMatch(matchId);
    }

    public List<BuyItemProjection> findPurchasedItems(long matchId, String heroName) {
        return purchasedItemEventRepository.findAllByMatch_IdAndAndHeroName(matchId, heroName);
    }

    public List<SpellCastsCount> countSpellCastInMatchByHeroName(long matchId, String heroName) {
        return spellCastEventRepository.countSpellCastInMatchByHeroName(matchId, heroName);
    }

    public List<DamageCount> countDamageInMatchByHero(long matchId, String heroName) {
        return hitEventRepository.countDamageInMatchByHero(matchId, heroName);
    }
}
