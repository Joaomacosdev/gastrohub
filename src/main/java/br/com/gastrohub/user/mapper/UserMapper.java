package br.com.gastrohub.user.mapper;

import br.com.gastrohub.address.entity.Address;
import br.com.gastrohub.address.mapper.AddressMapper;
import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.dto.response.UserResponseDTO;
import br.com.gastrohub.user.entity.User;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AddressMapper.class,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS

)
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "address")
    User toEntity(UserRequestDTO userRequestDTO);

    @Mapping(target = "address", source = "address")
    UserResponseDTO toResponseDTO(User user);


    @AfterMapping
    default void linkAddresses(@MappingTarget User user) {
        if (user.getAddress() != null) {
            List<Address> addresses = new ArrayList<>(user.getAddress());
            user.getAddress().clear();

            addresses.forEach(user::addAddress);
        }
    }

}
