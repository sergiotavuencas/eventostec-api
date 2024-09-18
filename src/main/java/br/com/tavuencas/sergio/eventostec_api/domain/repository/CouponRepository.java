package br.com.tavuencas.sergio.eventostec_api.domain.repository;

import br.com.tavuencas.sergio.eventostec_api.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    List<Coupon> findByEventIdAndValidAfter(UUID eventId, Date currentDate);
}
