package com.caspiantech.user.ms.mapper;

import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//import org.mapstruct.factory.Mappers;

//@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface UserMapper {
//
//    UserDto toUserDto(UserEntity userEntity);
//
//    void updateUserEntityFromDto(UserDto userDto, @MappingTarget UserEntity userEntity);
//}
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserDto toUserDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    public UserEntity toUserEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }

    public void updateUserEntityFromDto(UserDto userDto, UserEntity userEntity) {
        if (userDto.getName() != null) userEntity.setName(userDto.getName());
        if (userDto.getAge() != null) userEntity.setAge(userDto.getAge());
        if (userDto.getRegion() != null) userEntity.setRegion(userDto.getRegion());
        if (userDto.getSalary() != null) userEntity.setSalary(userDto.getSalary());
        if (userDto.getEmail() != null) userEntity.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null) userEntity.setPassword(userDto.getPassword());
        if (userDto.getAccountStatus() != null) userEntity.setAccountStatus(userDto.getAccountStatus());
    }
}
