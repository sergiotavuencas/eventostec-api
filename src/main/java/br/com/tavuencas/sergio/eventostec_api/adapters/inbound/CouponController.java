package br.com.tavuencas.sergio.eventostec_api.adapters.inbound;

import br.com.tavuencas.sergio.eventostec_api.application.dto.CouponRequestDto;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Coupon;
import br.com.tavuencas.sergio.eventostec_api.domain.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId, CouponRequestDto request) {
        Coupon coupon = service.addCouponToEvent(eventId, request);
        return ResponseEntity.ok(coupon);
    }
}
