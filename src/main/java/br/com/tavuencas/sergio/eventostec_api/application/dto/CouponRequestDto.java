package br.com.tavuencas.sergio.eventostec_api.application.dto;

public record CouponRequestDto(
        String code,
        Integer discount,
        Long valid) {
}
