package gg.bayes.challenge.domain.event;

import gg.bayes.challenge.domain.match.Match;
import lombok.Builder;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

public class EventMeta extends Event {

    @Builder
    public EventMeta(@NonNull Long startedAt,
                     @NotNull String heroName,
                     @NonNull Match match) {
        super(startedAt, heroName, match);
    }
}
