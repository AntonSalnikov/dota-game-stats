package gg.bayes.challenge.domain.event;

import gg.bayes.challenge.domain.BaseDomainObject;
import gg.bayes.challenge.domain.match.Match;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Getter@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"match"})
@EqualsAndHashCode(callSuper = true, exclude = {"match"})
@MappedSuperclass
@AllArgsConstructor
public abstract class Event extends BaseDomainObject {

    @Column(name = "started_at")
    @NonNull
    private Long startedAt;

    @Column(name = "hero_name")
    @NotNull
    private String heroName;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Match.class)
    @JoinColumn(name = "match_id")
    private Match match;
}
