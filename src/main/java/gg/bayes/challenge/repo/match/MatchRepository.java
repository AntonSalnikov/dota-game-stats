package gg.bayes.challenge.repo.match;

import gg.bayes.challenge.domain.match.Match;
import gg.bayes.challenge.repo.GenericJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends GenericJpaRepository<Match> {
}
