package com.caspiantech.user.ms.service.Impl;


import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.dao.repository.UserRepository;
import com.caspiantech.user.ms.exceptions.UserNotFoundException;
import com.caspiantech.user.ms.mapper.UserMapper;
import com.caspiantech.user.ms.model.dto.RegionAverageSalaryProjection;
import com.caspiantech.user.ms.model.dto.UserDto;
import com.caspiantech.user.ms.model.enums.AccountStatus;
import com.caspiantech.user.ms.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                 .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
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
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toUserDto);
    }

    @Override
    public List<UserDto> getUsersByAccountStatus(String accountStatus) {
        List<UserEntity> userEntities = userRepository.findByAccountStatus(AccountStatus.valueOf(accountStatus));
        return userEntities.stream()
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
    public UserDto updateUser(Long id,UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        // Map userDto properties to userEntity
        logger.info("Before update: {}", userEntity);
        userMapper.updateUserEntityFromDto(userDto, userEntity);
        logger.info("After update: {}", userEntity);
        return userMapper.toUserDto(userEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getUsersInHighestAverageSalaryRegion() {
        List<RegionAverageSalaryProjection> regionsWithAvgSalary = userRepository.findRegionsWithHighestAverageSalary();
        if (regionsWithAvgSalary.isEmpty()) {
            return Collections.emptyList();
        }
        String topRegion = regionsWithAvgSalary.get(0).getRegion();
        List<UserEntity> usersInTopRegion = userRepository.findByRegionIgnoreCaseContaining(topRegion);
        return usersInTopRegion.stream()
                .map(userMapper::toUserDto)
                .sorted(Comparator.comparingDouble(UserDto::getSalary))
                .collect(Collectors.toList());
    }
}