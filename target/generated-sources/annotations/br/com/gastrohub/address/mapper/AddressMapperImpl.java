package br.com.gastrohub.address.mapper;

import br.com.gastrohub.address.dto.response.AddressResponseDTO;
import br.com.gastrohub.address.entity.Address;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T18:58:00-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressResponseDTO toResponseDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        UUID id = null;
        String cep = null;
        String rua = null;
        String numero = null;
        String cidade = null;

        id = address.getId();
        cep = address.getCep();
        rua = address.getRua();
        numero = address.getNumero();
        cidade = address.getCidade();

        AddressResponseDTO addressResponseDTO = new AddressResponseDTO( id, cep, rua, numero, cidade );

        return addressResponseDTO;
    }
}
