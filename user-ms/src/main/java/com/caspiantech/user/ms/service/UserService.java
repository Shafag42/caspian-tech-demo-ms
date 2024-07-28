package com.caspiantech.user.ms.service;


import com.caspiantech.user.ms.model.dto.UserDto;
import com.caspiantech.user.ms.model.enums.AccountStatus;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface  UserService {
    UserDetailsService userDetailsService();

    UserDto findOne(Long id);

    List<UserDto> findAll();

    List<UserDto> getUsersByRegion(String region);

    @Transactional
    UserDto updateUser(Long id, UserDto userDto);

    @Transactional
    void delete(Long id);

    void updateAccountStatus(Long userId, AccountStatus newStatus);

    List<UserDto> getUsersInHighestAverageSalaryRegion();
}

