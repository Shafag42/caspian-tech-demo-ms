package com.caspiantech.user.ms.service.Impl;


import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.dao.repository.UserRepository;
import com.caspiantech.user.ms.exceptions.UnauthorizedException;
import com.caspiantech.user.ms.exceptions.UserNotFoundException;
import com.caspiantech.user.ms.mapper.UserMapper;
import com.caspiantech.user.ms.model.dto.UserDto;
import com.caspiantech.user.ms.model.enums.AccountStatus;
import com.caspiantech.user.ms.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@EnableWebSecurity
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;
    private final String notificationQueueName = "notificationQueue";

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                 .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                 .orElseThrow(UserNotFoundException::new);

        // Send login notification message
        String message = "User " + email + " login oldu";
        rabbitTemplate.convertAndSend(notificationQueueName, message);

        // Create a UserDetails object using the user’s information
        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                new ArrayList<>()); // No authorities for now
    }

    @Override
    public UserDto findOne(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersByRegion(String region) {
        List<UserEntity> userEntities = userRepository.findByRegionIgnoreCaseContaining(region);
        return userEntities.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id,UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        // Map userDto properties to userEntity
        userMapper.updateUserEntityFromDto(userDto, userEntity);

        // Save the updated user entity
        userEntity = userRepository.save(userEntity);

        return userMapper.toUserDto(userEntity);
    }
//
//    @Override
//    public UserProfile patchUserProfile(Long id, Map<String, Object> fields) {
//        UserEntity existingUserEntity = userRepository.findById(id)
//                .orElseThrow(UserNotFoundException::new);
//
//        UserEntity finalExistingUserEntity = existingUserEntity;
//        fields.forEach((k, v) -> {
//            Field field = ReflectionUtils.findField(UserEntity.class, k);
//            if (field != null) {
//                field.setAccessible(true);
//                Object value = v;
//                // Handle type conversion if necessary
//                if (field.getType().equals(Long.class) && v instanceof Integer) {
//                    value = Long.valueOf((Integer) v);
//                } else if (field.getType().equals(BigDecimal.class) && v instanceof String) {
//                    value = new BigDecimal((String) v);
//                }
//                ReflectionUtils.setField(field, finalExistingUserEntity, value);
//            }
//        });
//
//        existingUserEntity = userRepository.save(existingUserEntity);
//        return userMapper.toUserProfile(existingUserEntity);
//    }

    @Override
    public void updateAccountStatus(Long id, AccountStatus newStatus) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userEntity.setAccountStatus(newStatus);
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDto> getUsersInHighestAverageSalaryRegion() {
        // Regionların maaş ortalaması ilə sıralanmasını əldə edin
        List<Object[]> regionsWithAvgSalary = userRepository.findRegionsWithHighestAverageSalary();

        if (regionsWithAvgSalary.isEmpty()) {
            return Collections.emptyList();
        }

        // Ən yüksək maaş ortalamasına sahib olan regionu tapın
        String topRegion = (String) regionsWithAvgSalary.get(0)[0];

        // Ən yüksək maaş ortalamasına sahib olan regionda olan istifadəçiləri tapın
        List<UserEntity> usersInTopRegion = userRepository.findByRegionIgnoreCaseContaining(topRegion);

        // İstifadəçiləri DTO-ya çevirin
        return usersInTopRegion.stream()
                .map(userMapper::toUserDto)
                .sorted(Comparator.comparingDouble(UserDto::getSalary))
                .collect(Collectors.toList());
    }

}