package gg.bayes.challenge.domain;


import gg.bayes.challenge.util.OffsetDateTimeProvider;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@MappedSuperclass
@Data
public abstract class BaseDomainObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_time")
    @NotNull
    private OffsetDateTime createTime;

    @PrePersist
    protected void prePersist() {
        this.createTime = OffsetDateTimeProvider.now();
    }
}
