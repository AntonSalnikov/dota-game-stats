package gg.bayes.challenge.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroKills {
    private String hero;
    private Integer kills;
}
