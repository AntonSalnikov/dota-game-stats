package gg.bayes.challenge.util;

import gg.bayes.challenge.domain.event.Event;
import gg.bayes.challenge.domain.event.EventMeta;
import gg.bayes.challenge.domain.event.HeroKilledEvent;
import gg.bayes.challenge.domain.event.HitEvent;
import gg.bayes.challenge.domain.event.PurchasedItemEvent;
import gg.bayes.challenge.domain.event.SpellCastEvent;
import gg.bayes.challenge.domain.match.Match;
import gg.bayes.challenge.rest.model.EventType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
public class LogParser {

    private LogParser() {
    }

    /**
     * Parse string of log representation
     *
     * @param line log line to be processed
     * @return fully constructed event or null if it was not defined or not expected
     *
     * @throws NullPointerException if {@code line} or {@code match} are {@code null}
     * @throws IllegalArgumentException if line processing fails for any reason;
     */
    public static Event parse(@NonNull String line, @NonNull Match match) {
        log.trace("parsing line: {}", line);

        String[] parts = line.replaceFirst("is killed", "is_killed") //Dirty hack to make `is killed` event fit to action pattern
                .split("\\s");
        EventType type = EventType.parse(parts[2]);

        if(EventType.OTHER.equals(type)) {
            log.warn("Type was not defined properly or is not expected");
            return  null;
        }

        return toEvent(type, match, parts);
    }

    /**
     *
     * @param durationPart timestamp of log creation
     * @return number of milliseconds from the game start
     *
     * @throws NullPointerException if {@code durationPart} is {@code null}
     * @throws IllegalArgumentException if string is not in expexted format
     */
    protected static long extractDuration(@NonNull String durationPart) {

        //TODO: anton.salnikov - add timestamp matching filter to make this method more safe
        return Optional.ofNullable(durationPart)
                .filter(f -> f.length() == 14)
                .map(s -> toMilliseconds(s, 1, 3, HOURS) + toMilliseconds(s, 4, 6, MINUTES)
                        + toMilliseconds(s, 7, 9, SECONDS) + toMilliseconds(s, 10, 13, MILLIS))
                .orElseThrow(() -> new IllegalArgumentException(String.format("'%s' is not parsable", durationPart)));
    }

    /**
     * Extracts the name of the hero
     *
     * @param part to be processed
     * @return the name of the hero
     *
     * @throws IllegalArgumentException if {@code name} specified in unexpected format
     */
    protected static String extractName(String part) {


        if(StringUtils.startsWith(part, "npc_dota_hero_")) {
            return part.substring(14);
        }

        if(StringUtils.startsWith(part, "npc_dota_")) {
            return part.substring(9);
        }

        if(StringUtils.startsWith(part, "item_")) {
            return part.substring(5);
        }

        if(StringUtils.startsWith(part, "dota_unknown")) {
            return part;
        }

        log.warn("Part '{}' is not expected", part);
        throw new IllegalArgumentException(String.format("Name is not extracted from '%s'", part));
    }

    private static long toMilliseconds(String part, int start, int end, TemporalUnit temporalUnitUnit) {
        return Duration.of(NumberUtils.toLong(part.substring(start, end)), temporalUnitUnit).toMillis();
    }

    //TODO: anton.salnikov - method is to complex. Consider to split.
    private static Event toEvent(EventType type, Match match, String... parts) {

        String actor = parts[1];
        EventMeta meta = EventMeta.builder()
                .heroName(extractName(actor))
                .match(match)
                .startedAt(extractDuration(parts[0]))
                .build();

        switch (type) {
            //[00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars
            case HEROES_KILLING_EACH_OTHER: {

                String killedBy = parts[4];
                if(isHero(actor) && isHero(killedBy)) { //* Heroes killing each other

                    return HeroKilledEvent.builder()
                            .meta(meta)
                            .killedBy(extractName(killedBy))
                            .build();
                }
                return null;
            }
            //[00:10:58.827] npc_dota_hero_puck buys item item_circlet
            case ITEMS_BEING_PURCHASED: return PurchasedItemEvent.builder()
                    .meta(meta)
                    .itemName(extractName(parts[4]))
                    .build();
            //[00:11:17.389] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 18 damage (538->520)
            case DAMAGE_BEING_DONE: {

                String target = parts[3];
                if(isHero(target)) { //Damage being done to a hero

                    return HitEvent.builder()
                            .meta(meta)
                            .target(extractName(target))
                            .item(parts[5])
                            .damage(NumberUtils.toInt(parts[7]))
                            .build();
                }
                return null;
            }
            //[00:12:14.542] npc_dota_hero_puck casts ability puck_illusory_orb (lvl 1) on dota_unknown
            case SPELLS_BEING_CAST: return SpellCastEvent.builder()
                    .meta(meta)
                    .spellName(parts[4])
                    .build();
            default: return null;
        }
    }

    private static boolean isHero(String part) {
        return part.contains("_hero_");
    }
}
