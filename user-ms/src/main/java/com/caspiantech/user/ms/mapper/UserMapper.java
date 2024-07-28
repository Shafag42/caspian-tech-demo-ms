package com.caspiantech.user.ms.mapper;

import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(UserEntity userEntity);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "salary", target = "salary")
    @Mapping(source = "region", target = "region")
    @Mapping(source = "password", target = "password")
    UserEntity toUserEntity(UserDto userDto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "salary", target = "salary")
    @Mapping(source = "region", target = "region")
    @Mapping(source = "password", target = "password")
    void updateUserEntityFromDto(UserDto userDto, @MappingTarget UserEntity userEntity);

}
