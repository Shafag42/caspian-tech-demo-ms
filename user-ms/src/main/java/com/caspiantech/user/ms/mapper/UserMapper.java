package com.caspiantech.user.ms.mapper;

import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
//    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(UserEntity userEntity);

    UserEntity toUserEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    void updateUserEntityFromDto(UserDto userDto, @MappingTarget UserEntity userEntity);

}
