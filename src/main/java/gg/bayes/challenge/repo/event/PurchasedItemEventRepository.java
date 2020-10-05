package gg.bayes.challenge.repo.event;

import gg.bayes.challenge.domain.event.PurchasedItemEvent;
import gg.bayes.challenge.domain.projection.BuyItemProjection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasedItemEventRepository extends EventRepository<PurchasedItemEvent> {


    List<BuyItemProjection> findAllByMatch_IdAndAndHeroName(long matchId, String heroName);
}
