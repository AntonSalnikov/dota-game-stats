package gg.bayes.challenge.service;

import gg.bayes.challenge.domain.projection.BuyItemProjection;
import gg.bayes.challenge.domain.projection.DamageCount;
import gg.bayes.challenge.domain.projection.HeroKillsCount;
import gg.bayes.challenge.domain.projection.SpellCastsCount;

import java.util.List;

public interface MatchService {

    /**
     * Ingests game log into the database.
     * Performs filtering depending on event type according to rules:
     *
     * <ul>
     *   <li>Items being purchased</li>
     *   <li>Heroes killing each other</li>
     *   <li>Spells being cast (also has the level of the spell, there are multiple different levels for each spell)</li>
     *   <li>
     *       Damage being done to a hero (the word "hits" gives this away) could be from:
     *       <ul>
     *           <li> A normal attack (signified by dota_unknown as the damage type)</li>
     *           <li> A spell or item</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * All other events (items being used, buildings taking damage, gamestate changes, healing done, etc) are be ignored.
     *
     * @param payload to be processed
     *
     * @return match id
     *
     * @throws NullPointerException if {@code payload} is {@code null}
     */
    Long ingestMatch(String payload);

    /**
     * Counts number of kills made by ech hero in the game
     *
     * @param matchId specified match id
     * @return list with results
     */
    List<HeroKillsCount> countHeroKillsInMatch(long matchId);

    List<BuyItemProjection> findPurchasedItems(long matchId, String heroName);

    List<SpellCastsCount> countSpellCastInMatchByHeroName(long matchId, String heroName);

    List<DamageCount> countDamageInMatchByHero(long matchId, String heroName);
}
