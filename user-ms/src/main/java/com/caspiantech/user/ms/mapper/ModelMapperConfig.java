package com.caspiantech.user.ms.mapper;

import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.model.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<UserDto, UserEntity>() {
            @Override
            protected void configure() {
                map().setPassword(source.getPassword());
            }
        });
        return new ModelMapper();
    }
}
