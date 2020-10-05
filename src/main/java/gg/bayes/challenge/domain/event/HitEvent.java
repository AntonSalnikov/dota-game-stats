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
import javax.validation.constraints.NotNull;

@Entity(name = "hits")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter@Setter
@NoArgsConstructor
public class HitEvent extends Event {

    @Column(name = "target")
    @NotNull
    private String target;

    @Column(name = "item")
    @NotNull
    private String item;

    @Column
    @NotNull
    private Integer damage;

    @Builder
    public HitEvent(@NonNull EventMeta meta,
                    @NonNull String target,
                    @NonNull String item,
                    @NonNull Integer damage) {
        super(meta.getStartedAt(), meta.getHeroName(), meta.getMatch());
        this.target = target;
        this.item = item;
        this.damage = damage;
    }
}
