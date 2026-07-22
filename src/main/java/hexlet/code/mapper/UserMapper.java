package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Maps user DTOs to entity and back.
 * Field injection is required for MapStruct-generated Spring beans.
 */
@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@SuppressWarnings("java:S6813")
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO dto);

    public abstract UserDTO map(User user);

    @Mapping(target = "passwordDigest", source = "password")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User user);

    /**
     * Encodes password before create mapping.
     * @param data create DTO
     */
    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        var password = data.getPassword();
        data.setPassword(passwordEncoder.encode(password));
    }

    /**
     * Encodes password before update mapping when password is present.
     * @param data update DTO
     */
    @BeforeMapping
    public void encryptPassword(UserUpdateDTO data) {
        if (jsonNullableMapper.isPresent(data.getPassword())) {
            String password = jsonNullableMapper.unwrap(data.getPassword());
            String encoded = passwordEncoder.encode(password);
            data.setPassword(JsonNullable.of(encoded));
        }
    }
}
