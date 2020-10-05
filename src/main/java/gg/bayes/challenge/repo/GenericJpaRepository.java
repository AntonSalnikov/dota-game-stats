package gg.bayes.challenge.repo;


import gg.bayes.challenge.domain.BaseDomainObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base interface for JPA Repositories
 *
 * @author antin.salnikov
 */
@NoRepositoryBean
public interface GenericJpaRepository<T extends BaseDomainObject> extends JpaRepository<T, Long> {
}
