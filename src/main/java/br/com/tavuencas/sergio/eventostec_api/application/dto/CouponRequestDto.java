package br.com.tavuencas.sergio.eventostec_api.application.dto;

import java.util.Date;

public record CouponRequestDto(
        String code,
        Integer discount,
        Date valid) {
}
