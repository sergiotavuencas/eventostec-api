package br.com.tavuencas.sergio.eventostec_api.domain.repository;

import br.com.tavuencas.sergio.eventostec_api.domain.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.address a WHERE e.date >= :currentDate")
    Page<Event> findUpcomingEvents(@Param("currentDate") LocalDateTime currentDate, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "JOIN Address a ON e.id = a.event.id " +
            "WHERE (:city IS NULL OR a.city LIKE %:city%) " +
            "AND (:uf IS NULL OR a.uf LIKE %:uf%) " +
            "AND (e.date >= :startDate AND e.date <= :endDate)"
    )
    Page<Event> findFilteredEvents(@Param("city") String city,
                                   @Param("uf") String uf,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   Pageable pageable);
}
