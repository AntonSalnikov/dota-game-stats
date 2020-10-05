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

@Entity(name = "spell_casts")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter@Setter
@NoArgsConstructor
public class SpellCastEvent extends Event {

    @Column(name = "spell_name")
    private String spellName;

    @Builder
    public SpellCastEvent(@NonNull EventMeta meta,
                          @NonNull String spellName) {
        super(meta.getStartedAt(), meta.getHeroName(), meta.getMatch());
        this.spellName = spellName;
    }
}
