package br.com.tavuencas.sergio.eventostec_api.application.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record EventRequestDto(
        String title,
        String description,
        String city,
        String state,
        LocalDateTime date,
        Boolean remote,
        String eventUrl,
        MultipartFile image) {
}
