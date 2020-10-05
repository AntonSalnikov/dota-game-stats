package gg.bayes.challenge.domain.projection;

public interface DamageCount {

    String getTarget();
    int getDamageInstances();
    int getTotalDamage();
}
