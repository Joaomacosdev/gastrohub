package br.com.gastrohub.address.service;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.address.dto.request.AddressUpdateDTO;
import br.com.gastrohub.address.dto.response.AddressResponseDTO;
import br.com.gastrohub.address.entity.Address;
import br.com.gastrohub.address.mapper.AddressMapper;
import br.com.gastrohub.address.repository.AddressRepository;
import br.com.gastrohub.infra.exception.NotFoundException;
import br.com.gastrohub.user.entity.User;
import br.com.gastrohub.user.repository.UserRepository;
import br.com.gastrohub.user.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressCommandService, AddressQueryService {

    private final AddressRepository addressRepository;
    private final AddressMapper mapper;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper mapper, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }


    @Transactional
    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO dto, UUID userId) {

        logger.debug("Criando endereço com cep: {}", dto.cep());

        User user = searchUser(userId);

        Address address = mapper.toEntity(dto);
        address.setUser(user);

        Address addressSaved = addressRepository.save(address);

        return mapper.toResponseDTO(addressSaved);
    }

    @Transactional(readOnly = true)
    @Override
    public AddressResponseDTO findById(UUID id) {
        logger.debug("Buscando endereço por ID: {}", id);

        Address address = searchAddress(id);

        return mapper.toResponseDTO(address);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AddressResponseDTO> findAll() {
        logger.info("Listando todos os endereço");

        return addressRepository.findAll().stream().map(mapper::toResponseDTO).toList();
    }


    @Transactional
    @Override
    public AddressResponseDTO updateAddress(UUID id, AddressUpdateDTO dto) {
        logger.info("Atualizando endereço ID: {}", id);

        Address address = searchAddress(id);

        address.updateAddress(
                dto.cep(),
                dto.rua(),
                dto.numero(),
                dto.cidade(),
                dto.estado()
        );

        return mapper.toResponseDTO(address);
    }

    @Override
    public void deleteAddress(UUID id) {
        logger.info("Deletando endereço ID: {}", id);

        Address address = searchAddress(id);
        addressRepository.delete(address);

    }

    private Address searchAddress(UUID id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Endereço", id));
    }

    private User searchUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário", id));
    }
}
