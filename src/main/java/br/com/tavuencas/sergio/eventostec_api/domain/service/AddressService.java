package br.com.tavuencas.sergio.eventostec_api.domain.service;

import br.com.tavuencas.sergio.eventostec_api.application.dto.EventRequestDto;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Address;
import br.com.tavuencas.sergio.eventostec_api.domain.model.Event;
import br.com.tavuencas.sergio.eventostec_api.domain.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public Address createAddress(EventRequestDto request, Event event) {
        Address address = new Address();
        address.setCity(request.city());
        address.setUf(request.state());
        address.setEvent(event);

        return repository.save(address);
    }
}
