package gg.bayes.challenge.domain.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "heroes_killings")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter@Setter
@NoArgsConstructor
public class HeroKilledEvent extends Event {

    @Column(name = "killed_by")
    private String killedBy;

    @Builder
    public HeroKilledEvent(@NonNull EventMeta meta,
                           @NonNull String killedBy) {
        super(meta.getStartedAt(), meta.getHeroName(), meta.getMatch());
        this.killedBy = killedBy;
    }
}
