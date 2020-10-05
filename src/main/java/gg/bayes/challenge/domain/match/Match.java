package gg.bayes.challenge.domain.match;

import gg.bayes.challenge.domain.BaseDomainObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;

@Entity(name = "matches")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Match extends BaseDomainObject {

}
