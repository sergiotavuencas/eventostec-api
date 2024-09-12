package br.com.tavuencas.sergio.eventostec_api.domain.repository;

import br.com.tavuencas.sergio.eventostec_api.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
