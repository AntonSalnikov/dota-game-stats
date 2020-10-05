package gg.bayes.challenge.util

import gg.bayes.challenge.domain.event.HeroKilledEvent
import gg.bayes.challenge.domain.event.HitEvent
import gg.bayes.challenge.domain.event.PurchasedItemEvent
import gg.bayes.challenge.domain.event.SpellCastEvent
import gg.bayes.challenge.domain.match.Match
import spock.lang.Specification
import spock.lang.Unroll

class LogParserTest extends Specification {

    def match = new Match(id: 1L)

    @Unroll
    def "duration is successfully extracted from #durationPart"() {
        expect:
        LogParser.extractDuration(durationPart) == result

        where:
        durationPart     || result
        "[00:00:00.000]" || 0L
        "[00:09:43.879]" || 583879L
        "[00:37:22.851]" || 2242851L
        "[99:59:59.999]" || 359999999L
    }

    @Unroll
    def "Hero name is successfully extracted from #name"() {
        expect:
        LogParser.extractName(name) == result

        where:
        name                      || result
        "npc_dota_hero_pangolier" || "pangolier"
        "npc_dota_hero_bane"      || "bane"
        "npc_dota_hero_snapfire"  || "snapfire"
        "npc_dota_hero_rubick"    || "rubick"
    }

    def "HeroKilledEvent is successfully parsed from the line"() {
        given:
        def line = '[00:31:39.424] npc_dota_hero_snapfire is killed by npc_dota_hero_bloodseeker'

        when:
        def result = LogParser.parse(line, match)

        then:
        noExceptionThrown()
        result != null

        result instanceof HeroKilledEvent
        def event = result as HeroKilledEvent

        event.match == match
        event.startedAt == 1899424
        event.heroName == "snapfire"
        event.killedBy == "bloodseeker"
    }

    def "HeroKilledEvent is null because actors are not heros"() {
        given:
        def line = '[00:12:21.207] npc_dota_neutral_harpy_storm is killed by npc_dota_creep_goodguys_ranged'

        when:
        def result = LogParser.parse(line, match)

        then:
        noExceptionThrown()
        result == null
    }

    def "HitEvent is successfully parsed from the line"() {
        given:
        def line = '[00:16:17.116] npc_dota_hero_death_prophet hits npc_dota_hero_dragon_knight with dota_unknown for 56 damage (1263->1207)'

        when:
        def result = LogParser.parse(line, match)

        then:
        noExceptionThrown()
        result != null

        result instanceof HitEvent
        def event = result as HitEvent

        event.match == match
        event.startedAt == 977116
        event.heroName == "death_prophet"
        event.target == "dragon_knight"
        event.item == "dota_unknown"
        event.damage == 56
    }

    def "PurchasedItemEvent is successfully parsed from the line"() {
        given:
        def line = '[00:17:50.260] npc_dota_hero_bane buys item item_boots'

        when:
        def result = LogParser.parse(line, match)

        then:
        noExceptionThrown()
        result != null

        result instanceof PurchasedItemEvent
        def event = result as PurchasedItemEvent

        event.match == match
        event.startedAt == 1070260
        event.heroName == "bane"
        event.itemName == "boots"
    }

    def "SpellCastEvent is successfully parsed from the line"() {
        given:
        def line = '[00:18:14.154] npc_dota_hero_abyssal_underlord casts ability abyssal_underlord_firestorm (lvl 3) on dota_unknown'

        when:
        def result = LogParser.parse(line, match)

        then:
        noExceptionThrown()
        result != null

        result instanceof SpellCastEvent
        def event = result as SpellCastEvent

        event.match == match
        event.startedAt == 1094154
        event.heroName == "abyssal_underlord"
        event.spellName == "abyssal_underlord_firestorm"
    }
}
