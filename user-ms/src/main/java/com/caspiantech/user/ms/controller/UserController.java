package com.caspiantech.user.ms.controller;


import com.caspiantech.user.ms.dao.repository.UserRepository;
import com.caspiantech.user.ms.model.dto.UserDto;
import com.caspiantech.user.ms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.findOne(userId);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        return userService.updateUser(userId,userDto);
    }

    @GetMapping("/")
    public Page<UserDto> getUsers(@RequestParam(defaultValue = "0") int pageNo,
                                  @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return userService.findAll(pageable);
    }

    @GetMapping("/region")
    public List<UserDto> getUsersByRegion(@RequestParam("region_name") String region) {
        return userService.getUsersByRegion(region);
    }

    @GetMapping("/status")
    public List<UserDto> getUsersByAccountStatus(@RequestParam("account_status") String accountStatus){
        return userService.getUsersByAccountStatus(accountStatus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.delete(id);
    }

    @GetMapping("/highest-salary-region")
    public Page<UserDto> getUsersInHighestAverageSalaryRegion(@RequestParam(defaultValue = "0") int pageNo,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return userService.getUsersInHighestAverageSalaryRegion(pageable);
    }
}