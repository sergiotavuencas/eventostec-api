package br.com.tavuencas.sergio.eventostec_api.adapters.inbound;

import br.com.tavuencas.sergio.eventostec_api.application.dto.EventDetailsDto;
import br.com.tavuencas.sergio.eventostec_api.application.dto.EventRequestDto;
import br.com.tavuencas.sergio.eventostec_api.application.dto.EventResponseDto;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Event;
import br.com.tavuencas.sergio.eventostec_api.domain.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam("remote") Boolean remote,
            @RequestParam("eventUrl") String eventUrl,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {

        EventRequestDto dto = new EventRequestDto(title, description, city, state, date, remote, eventUrl, image);
        Event newEvent = this.service.create(dto);

        return ResponseEntity.ok(newEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailsDto> getEventDetails(@PathVariable("id") UUID id) {
        EventDetailsDto response = this.service.getEventById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<EventResponseDto> allEvents = this.service.getUpcomingEvents(page, size);
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDto>> getFilteredEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<EventResponseDto> events = this.service.getFilteredEvents(page, size, city, uf, startDate, endDate);
        return ResponseEntity.ok(events);
    }
}
