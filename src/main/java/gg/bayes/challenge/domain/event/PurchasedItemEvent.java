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

@Entity(name = "purchased_items")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter@Setter
@NoArgsConstructor
public class PurchasedItemEvent extends Event {

    @Column(name = "item_name")
    @NotNull
    private String itemName;

    @Builder
    public PurchasedItemEvent(@NonNull EventMeta meta,
                              @NonNull String itemName) {
        super(meta.getStartedAt(), meta.getHeroName(), meta.getMatch());
        this.itemName = itemName;
    }
}
