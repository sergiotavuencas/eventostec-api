package br.com.tavuencas.sergio.eventostec_api.domain.service;

import br.com.tavuencas.sergio.eventostec_api.application.dto.EventRequestDto;
import br.com.tavuencas.sergio.eventostec_api.application.dto.EventResponseDto;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Event;
import br.com.tavuencas.sergio.eventostec_api.domain.repository.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    private final AmazonS3 s3Client;

    private final EventRepository repository;

    private final AddressService addressService;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public EventService(AmazonS3 s3Client, EventRepository repository, AddressService addressService) {
        this.s3Client = s3Client;
        this.repository = repository;
        this.addressService = addressService;
    }

    public Event create(EventRequestDto request) {
        String imgUrl = null;

        if (request.image() != null) {
            imgUrl = this.uploadImg(request.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(request.title());
        newEvent.setDescription(request.description());
        newEvent.setImgUrl(imgUrl);
        newEvent.setEventUrl(request.eventUrl());
        newEvent.setRemote(request.remote());
        newEvent.setDate(request.date());

        this.repository.save(newEvent);

        if (Boolean.FALSE.equals(request.remote())) {
            this.addressService.createAddress(request, newEvent);
        }

        return newEvent;
    }

    public List<EventResponseDto> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Event> eventsPage = this.repository.findUpcomingEvents(LocalDateTime.now(), pageable);

        return eventsPage.map(event -> new EventResponseDto(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getUf() : "",
                        event.getDate(),
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl()))
                .stream().toList();
    }

    public List<EventResponseDto> getFilteredEvents(int page, int size, String city, String uf, LocalDateTime startDate, LocalDateTime endDate) {
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : LocalDateTime.now();
        endDate = (endDate != null) ? endDate : LocalDateTime.of(2030, 12, 31, 23, 59, 59, 599);

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = this.repository.findFilteredEvents(city, uf, startDate, endDate, pageable);

        return eventsPage.map(event -> new EventResponseDto(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getUf() : "",
                        event.getDate(),
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl()))
                .stream().toList();
    }

    private String uploadImg(MultipartFile multipartFile) {
        String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.converMultipartToFile(multipartFile);
            s3Client.putObject(bucketName, filename, file);

            cleanUp(Path.of(file.getPath()));

            return s3Client.getUrl(bucketName, filename).toString();

        } catch (Exception e) {
            throw new AmazonS3Exception("");
        }
    }

    private File converMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convert = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        try (FileOutputStream fos = new FileOutputStream(convert)) {
            fos.write(multipartFile.getBytes());
            return convert;
        } catch (IOException e) {
            throw new IOException("Error converting file.");
        }
    }

    private void cleanUp(Path path) throws IOException {
        try {
            Files.delete(path);
        } catch (Exception e) {
            throw new NoSuchFileException("File not found");
        }
    }
}
