package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.domain.match.Match;
import gg.bayes.challenge.domain.projection.BuyItemProjection;
import gg.bayes.challenge.domain.projection.DamageCount;
import gg.bayes.challenge.domain.projection.HeroKillsCount;
import gg.bayes.challenge.domain.projection.SpellCastsCount;
import gg.bayes.challenge.repo.event.EventRepositoryFacade;
import gg.bayes.challenge.repo.match.MatchRepository;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.util.LogParser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final EventRepositoryFacade eventRepositoryFacade;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository,
                            EventRepositoryFacade eventRepositoryFacade) {
        this.matchRepository = matchRepository;
        this.eventRepositoryFacade = eventRepositoryFacade;
    }

    @Override
    @Transactional
    public Long ingestMatch(String payload) {
        log.debug("Payload is processing ...");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Ingest match");
        Match match = matchRepository.save(new Match());

        try (Stream<String> streamLine = Files.lines(createTempFile(payload, "match-" + match.getId() + "-"))) {

            //Lest make this solution `fail fast`. If any error appears we will terminate upload and revert commit.
            streamLine
                    .filter(StringUtils::isNotBlank)
                    .map(s -> LogParser.parse(s, match))
                    .filter(Objects::nonNull)
                    .forEach(eventRepositoryFacade::save);

            stopWatch.stop();
            log.debug("Match '{}' was successfully created. Operation took {}", match, stopWatch.prettyPrint());
            return match.getId();
        } catch (IOException e) {
            log.error("Error appeared while processing payload", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected exception appeared while processing payload");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HeroKillsCount> countHeroKillsInMatch(long matchId) {
        log.debug("Calculating kills in match with id {}", matchId);
        return eventRepositoryFacade.countKillsInMatch(matchId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyItemProjection> findPurchasedItems(long matchId, @NonNull String heroName) {
        log.debug("Searching for items purchasd by hero {} in match {}", heroName, matchId);
        return eventRepositoryFacade.findPurchasedItems(matchId, heroName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpellCastsCount> countSpellCastInMatchByHeroName(long matchId, @NonNull String heroName) {
        log.debug("Counting for spells casted by hero {} in match {}", heroName, matchId);
        return eventRepositoryFacade.countSpellCastInMatchByHeroName(matchId, heroName);
    }

    @Override
    public List<DamageCount> countDamageInMatchByHero(long matchId, @NonNull String heroName) {
        log.debug("Counting for damage done by hero {} in match {}", heroName, matchId);
        return eventRepositoryFacade.countDamageInMatchByHero(matchId, heroName);
    }

    private static Path createTempFile(String payload, String prefix) throws IOException {

        Path path = Files.createTempFile(prefix, ".txt");
        File file = path.toFile();
        Files.write(path, payload.getBytes(StandardCharsets.UTF_8));
        file.deleteOnExit();

        return path;
    }
}
