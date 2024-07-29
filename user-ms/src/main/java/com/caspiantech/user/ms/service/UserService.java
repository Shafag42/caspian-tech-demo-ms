package com.caspiantech.user.ms.service;


import com.caspiantech.user.ms.model.dto.UserDto;
import com.caspiantech.user.ms.model.enums.AccountStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface  UserService {
    UserDetailsService userDetailsService();

    UserDto findOne(Long id);

    public Page<UserDto> findAll(Pageable pageable);

    List<UserDto> getUsersByAccountStatus(String accountStatus);

    List<UserDto> getUsersByRegion(String region);

    @Transactional
    UserDto updateUser(Long id, UserDto userDto);

    @Transactional
    void delete(Long id);

    List<UserDto> getUsersInHighestAverageSalaryRegion();
}

