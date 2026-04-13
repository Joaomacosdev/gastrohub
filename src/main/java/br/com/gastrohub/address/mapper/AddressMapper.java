package br.com.gastrohub.address.mapper;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.address.dto.response.AddressResponseDTO;
import br.com.gastrohub.address.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    default Address toEntity(AddressRequestDTO dto) {
        return new Address(
                dto.cep(),
                dto.rua(),
                dto.numero(),
                dto.cidade()
        );
    }

    AddressResponseDTO toResponseDTO(Address address);

}
