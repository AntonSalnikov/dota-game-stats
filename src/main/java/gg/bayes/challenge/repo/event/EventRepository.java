package gg.bayes.challenge.repo.event;


import gg.bayes.challenge.domain.event.Event;
import gg.bayes.challenge.repo.GenericJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base interface for events repositories
 *
 * @author antin.salnikov
 */
@NoRepositoryBean
public interface EventRepository<T extends Event> extends GenericJpaRepository<T> {
}
