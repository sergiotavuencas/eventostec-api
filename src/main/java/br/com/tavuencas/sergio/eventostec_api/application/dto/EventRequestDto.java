package br.com.tavuencas.sergio.eventostec_api.application.dto;

import org.springframework.web.multipart.MultipartFile;

public record EventRequestDto(
        String title,
        String description,
        String city,
        String state,
        Long date,
        Boolean remote,
        String eventUrl,
        MultipartFile image) {
}
