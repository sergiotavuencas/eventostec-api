package br.com.tavuencas.sergio.eventostec_api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDto(
        UUID id,
        String title,
        String description,
        String city,
        String state,
        LocalDateTime date,
        Boolean remote,
        String eventUrl,
        String imgUrl) {
}
