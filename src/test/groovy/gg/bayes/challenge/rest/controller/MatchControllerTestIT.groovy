package gg.bayes.challenge.rest.controller

import gg.bayes.challenge.rest.model.HeroDamage
import gg.bayes.challenge.rest.model.HeroItems
import gg.bayes.challenge.rest.model.HeroKills
import gg.bayes.challenge.rest.model.HeroSpells
import gg.bayes.challenge.util.IntegrationTest
import org.apache.commons.io.IOUtils
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import static java.nio.charset.StandardCharsets.UTF_8

@IntegrationTest
class MatchControllerTestIT extends Specification {

    @LocalServerPort
    private int port

    private WebTestClient client

    def setup() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }


    def "First match is successfully ingested"() {
        given:
        def path = "/api/match"
        def requestPayload = fromResources("combatlog.txt")

        when:
        def result = client.post().uri(path)
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestPayload)
                .exchange()

        then:
        noExceptionThrown()
        result != null
        result.expectStatus().isOk()

        and:"Kills stats are successfully calculated"
        when:
        def killsResult = client.get().uri("/api/match/{matchId}", 1).exchange()

        then:
        noExceptionThrown()
        killsResult != null

        killsResult.expectStatus().isOk()
        killsResult.expectBodyList(HeroKills).hasSize(10)
                .contains(
                        new HeroKills("abyssal_underlord", 6),
                        new HeroKills("puck", 7),
                        new HeroKills("snapfire", 2),
                )


        and:"Each item purchase by hero in a match is successfully found"
        when:
        def itemPurchaseResult = client.get().uri("/api/match/{matchId}/{heroName}/items", 1, "rubick").exchange()

        then:
        noExceptionThrown()
        itemPurchaseResult != null

        itemPurchaseResult.expectStatus().isOk()
        itemPurchaseResult.expectBodyList(HeroItems).hasSize(26)
                .contains(
                        new HeroItems("sobi_mask", 530891),
                        new HeroItems("branches", 1350425),
                        new HeroItems("tpscroll", 2126890),
                )

        and:"Number of spells casts is successfully calculated for hero in the match"
        when:
        def spellCastsResult = client.get().uri("/api/match/{matchId}/{heroName}/spells", 1, "rubick").exchange()

        then:
        noExceptionThrown()
        spellCastsResult != null

        spellCastsResult.expectStatus().isOk()
        spellCastsResult.expectBodyList(HeroSpells).hasSize(10)
                .contains(
                        new HeroSpells("pangolier_swashbuckle", 2),
                        new HeroSpells("rubick_spell_steal", 9),
                        new HeroSpells("snapfire_spit_creep", 7),
                )

        and:"And damage is successfully calculated for hero in the match"
        when:
        def demageResult = client.get().uri("/api/match/{matchId}/{heroName}/damage", 1, "rubick").exchange()

        then:
        noExceptionThrown()
        demageResult != null

        demageResult.expectStatus().isOk()
        demageResult.expectBodyList(HeroDamage).hasSize(5)
                .contains(
                        new HeroDamage("abyssal_underlord", 19, 1577),
                        new HeroDamage("dragon_knight", 29, 2908),
                        new HeroDamage("snapfire", 30, 2236),
                )

    }

    String fromResources(filePath) {
        IOUtils.toString(this.class.getResourceAsStream(filePath), UTF_8.name())
    }
}
