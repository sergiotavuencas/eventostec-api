package br.com.tavuencas.sergio.eventostec_api.domain.service;

import br.com.tavuencas.sergio.eventostec_api.application.dto.CouponRequestDto;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Coupon;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Event;
import br.com.tavuencas.sergio.eventostec_api.domain.repository.CouponRepository;
import br.com.tavuencas.sergio.eventostec_api.domain.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    private final CouponRepository repository;

    private final EventRepository eventRepository;

    public CouponService(CouponRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDto request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon newCoupon = new Coupon();
        newCoupon.setCode(request.code());
        newCoupon.setDiscount(request.discount());
        newCoupon.setValid(request.valid());
        newCoupon.setEvent(event);

        return this.repository.save(newCoupon);
    }

    public List<Coupon> consultCoupons(UUID eventId, Date currentDate) {
        return this.repository.findByEventIdAndValidAfter(eventId, currentDate);
    }
}
