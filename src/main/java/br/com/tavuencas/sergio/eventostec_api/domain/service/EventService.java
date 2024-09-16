package br.com.tavuencas.sergio.eventostec_api.domain.service;

import br.com.tavuencas.sergio.eventostec_api.application.dto.EventRequestDto;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Event;
import br.com.tavuencas.sergio.eventostec_api.domain.repository.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    private final AmazonS3 s3Client;

    private final EventRepository repository;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public EventService(AmazonS3 s3Client, EventRepository repository) {
        this.s3Client = s3Client;
        this.repository = repository;
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
        newEvent.setDate(new Date(request.date()));

        repository.save(newEvent);

        return newEvent;
    }

    private String uploadImg(MultipartFile multipartFile) {
        String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.converMultipartToFile(multipartFile);

            s3Client.putObject(bucketName, filename, file);

            file.delete();

            return s3Client.getUrl(bucketName, filename).toString();

        } catch (Exception e) {
//            throw new RuntimeException(e);
            return "";
        }
    }

    private File converMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convert = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convert);
        fos.write(multipartFile.getBytes());
        fos.close();

        return convert;
    }
}
