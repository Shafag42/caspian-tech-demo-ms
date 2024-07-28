package com.caspiantech.user.ms.controller;


import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.dao.repository.UserRepository;
import com.caspiantech.user.ms.model.dto.UserDto;
import com.caspiantech.user.ms.model.enums.AccountStatus;
import com.caspiantech.user.ms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequestMapping("/v1/users")

public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.findOne(userId);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@RequestBody Long userId,UserDto userDto) {
        return userService.updateUser(userId,userDto);
    }

    @GetMapping("/")
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/region/{region_name}")
    public List<UserDto> getUsersByRegion(@PathVariable("region_name") String region) {
        return userService.getUsersByRegion(region);
    }

    @GetMapping("/status/{account_status}")
    public List<UserEntity> getUsersByAccountStatus(@PathVariable("account_status") String accountStatus) {
        AccountStatus status = AccountStatus.valueOf(accountStatus.toUpperCase());
        return userRepository.findByAccountStatus(status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.delete(id);
    }

    @GetMapping("/highest-salary-region")
    public List<UserDto> getUsersInHighestAverageSalaryRegion() {
        return userService.getUsersInHighestAverageSalaryRegion();
    }
}