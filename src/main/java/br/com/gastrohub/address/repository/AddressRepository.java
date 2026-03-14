package br.com.gastrohub.address.repository;

import br.com.gastrohub.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository< Address, UUID> {
}
