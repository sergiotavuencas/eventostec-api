package br.com.tavuencas.sergio.eventostec_api.adapters.inbound;

import br.com.tavuencas.sergio.eventostec_api.application.dto.CouponRequestDto;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Coupon;
import br.com.tavuencas.sergio.eventostec_api.domain.service.CouponService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponToEvent(
            @PathVariable UUID eventId,
            @RequestParam("code") String code,
            @RequestParam("discount") Integer discount,
            @RequestParam("valid") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date valid) {
        CouponRequestDto request = new CouponRequestDto(code, discount, valid);
        Coupon coupon = service.addCouponToEvent(eventId, request);
        return ResponseEntity.ok(coupon);
    }
}
