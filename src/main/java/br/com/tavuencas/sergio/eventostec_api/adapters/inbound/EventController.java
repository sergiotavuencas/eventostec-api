package br.com.tavuencas.sergio.eventostec_api.adapters.inbound;

import br.com.tavuencas.sergio.eventostec_api.application.dto.EventRequestDto;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Event;
import br.com.tavuencas.sergio.eventostec_api.domain.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> create(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("date") Long date,
            @RequestParam("remote") Boolean remote,
            @RequestParam("eventUrl") String eventUrl,
            @RequestParam(value = "image", required = false) MultipartFile image
            ) {

        EventRequestDto dto = new EventRequestDto(title, description, city, state, date, remote, eventUrl, image);
        Event newEvent = this.service.create(dto);

        return ResponseEntity.ok(newEvent);
    }
}
