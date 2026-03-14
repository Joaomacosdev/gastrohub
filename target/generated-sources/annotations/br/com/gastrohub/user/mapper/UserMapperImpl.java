package br.com.gastrohub.user.mapper;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.address.dto.response.AddressResponseDTO;
import br.com.gastrohub.address.entity.Address;
import br.com.gastrohub.address.mapper.AddressMapper;
import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.dto.response.UserResponseDTO;
import br.com.gastrohub.user.entity.User;
import br.com.gastrohub.user.entity.enums.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T18:58:00-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public User toEntity(UserRequestDTO userRequestDTO) {
        if ( userRequestDTO == null ) {
            return null;
        }

        User user = new User();

        List<Address> list = addressRequestDTOListToAddressList( userRequestDTO.address() );
        if ( list != null ) {
            user.setAddress( list );
        }
        if ( userRequestDTO.nome() != null ) {
            user.setNome( userRequestDTO.nome() );
        }
        if ( userRequestDTO.email() != null ) {
            user.setEmail( userRequestDTO.email() );
        }
        if ( userRequestDTO.login() != null ) {
            user.setLogin( userRequestDTO.login() );
        }
        if ( userRequestDTO.role() != null ) {
            user.setRole( userRequestDTO.role() );
        }

        linkAddresses( user );

        return user;
    }

    @Override
    public UserResponseDTO toResponseDTO(User user) {
        if ( user == null ) {
            return null;
        }

        List<AddressResponseDTO> address = null;
        UUID id = null;
        String nome = null;
        String email = null;
        String login = null;
        LocalDateTime dataUltimaAlteracao = null;
        Role role = null;

        List<AddressResponseDTO> list = addressListToAddressResponseDTOList( user.getAddress() );
        if ( list != null ) {
            address = list;
        }
        if ( user.getId() != null ) {
            id = user.getId();
        }
        if ( user.getNome() != null ) {
            nome = user.getNome();
        }
        if ( user.getEmail() != null ) {
            email = user.getEmail();
        }
        if ( user.getLogin() != null ) {
            login = user.getLogin();
        }
        if ( user.getDataUltimaAlteracao() != null ) {
            dataUltimaAlteracao = user.getDataUltimaAlteracao();
        }
        if ( user.getRole() != null ) {
            role = user.getRole();
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO( id, nome, email, login, dataUltimaAlteracao, address, role );

        return userResponseDTO;
    }

    protected List<Address> addressRequestDTOListToAddressList(List<AddressRequestDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Address> list1 = new ArrayList<Address>( list.size() );
        for ( AddressRequestDTO addressRequestDTO : list ) {
            list1.add( addressMapper.toEntity( addressRequestDTO ) );
        }

        return list1;
    }

    protected List<AddressResponseDTO> addressListToAddressResponseDTOList(List<Address> list) {
        if ( list == null ) {
            return null;
        }

        List<AddressResponseDTO> list1 = new ArrayList<AddressResponseDTO>( list.size() );
        for ( Address address : list ) {
            list1.add( addressMapper.toResponseDTO( address ) );
        }

        return list1;
    }
}
