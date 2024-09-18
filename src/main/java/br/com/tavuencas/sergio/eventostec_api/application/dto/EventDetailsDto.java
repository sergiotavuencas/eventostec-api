package br.com.tavuencas.sergio.eventostec_api.application.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record EventDetailsDto(
        UUID id,
        String title,
        String description,
        String city,
        String state,
        LocalDateTime date,
        Boolean remote,
        String eventUrl,
        String imgUrl,
        List<CouponDto> coupons) {

    public record CouponDto(
            String code,
            Integer discount,
            Date validUntil) {
    }
}
