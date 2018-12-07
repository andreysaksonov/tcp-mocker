package io.payworks.labs.tcpmocker.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordingsRepository extends PagingAndSortingRepository<Recording, Long> {

    Optional<Recording> findTopByOrderByEventTimeDesc();

    List<Recording> findAllByOrderByEventTimeDesc(Pageable pageable);
}
