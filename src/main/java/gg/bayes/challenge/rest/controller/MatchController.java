package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/match")
@Validated
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping(consumes = "text/plain")
    public Long ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
        log.debug("Ingesting log payload");

        return matchService.ingestMatch(payload);
    }

    @GetMapping("{matchId}")
    public List<HeroKills> getMatch(@PathVariable("matchId") Long matchId) {
        return matchService.countHeroKillsInMatch(matchId).stream()
                .map(s -> new HeroKills(s.getHero(), s.getKills()))
                .collect(Collectors.toList());
    }

    @GetMapping("{matchId}/{heroName}/items")
    public List<HeroItems> getItems(@PathVariable("matchId") Long matchId,
                                    @PathVariable("heroName") String heroName) {
        return matchService.findPurchasedItems(matchId, heroName).stream()
                .map(s -> new HeroItems(s.getItemName(), s.getStartedAt()))
                .collect(Collectors.toList());
    }

    @GetMapping("{matchId}/{heroName}/spells")
    public List<HeroSpells> getSpells(@PathVariable("matchId") Long matchId,
                                      @PathVariable("heroName") String heroName) {
        return matchService.countSpellCastInMatchByHeroName(matchId, heroName).stream()
                .map(s -> new HeroSpells(s.getSpell(), s.getCasts()))
                .collect(Collectors.toList());
    }

    @GetMapping("{matchId}/{heroName}/damage")
    public List<HeroDamage> getDamage(@PathVariable("matchId") Long matchId,
                                      @PathVariable("heroName") String heroName) {
        return matchService.countDamageInMatchByHero(matchId, heroName).stream()
                .map(s -> new HeroDamage(s.getTarget(), s.getDamageInstances(), s.getTotalDamage()))
                .collect(Collectors.toList());
    }
}
