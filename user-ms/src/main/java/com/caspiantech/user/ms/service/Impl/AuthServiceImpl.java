package com.caspiantech.user.ms.service.Impl;


import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.dao.repository.UserRepository;
import com.caspiantech.user.ms.exceptions.EmailAlreadyExistsException;
import com.caspiantech.user.ms.exceptions.UnauthorizedException;
import com.caspiantech.user.ms.exceptions.UserNotFoundException;
import com.caspiantech.user.ms.mapper.UserMapper;
import com.caspiantech.user.ms.model.dto.UserDto;
import com.caspiantech.user.ms.model.dto.request.LoginRequest;
import com.caspiantech.user.ms.model.dto.request.RegisterRequest;
import com.caspiantech.user.ms.model.dto.response.JwtAuthResponse;
import com.caspiantech.user.ms.model.enums.AccountStatus;
import com.caspiantech.user.ms.service.AuthService;
import com.caspiantech.user.ms.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        UserDto userDto = UserDto.builder()
                .name(registerRequest.getName())
                .age(registerRequest.getAge())
                .region(registerRequest.getRegion())
                .salary(registerRequest.getSalary())
                .email(registerRequest.getEmail())
                .accountStatus(AccountStatus.ENABLED)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
         System.out.println("UserDto: " + userDto); // Debugging
        UserEntity userEntity = userMapper.toUserEntity(userDto);
         System.out.println("User: " + userEntity); // Debugging
        userRepository.save(userEntity);

    }

    @Override
    public JwtAuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            String message = "User " + loginRequest.getEmail() + " login uğursuz oldu";
            rabbitTemplate.convertAndSend("notificationQueue", message);
            throw new UnauthorizedException("Invalid email or password");}

        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(UserNotFoundException::new);
        var jwt = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Send login notification message
        String message = "User " + loginRequest.getEmail() + " login oldu";
        String notificationQueueName = "notificationQueue";
        rabbitTemplate.convertAndSend(notificationQueueName, message);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(jwt);
        jwtAuthResponse.setRefreshToken(refreshToken);
        return jwtAuthResponse;
    }

    @Override
    public void logout() {
        // Get the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Invalidate the authentication object (sign-out)
        authentication.setAuthenticated(false);
        // Clear the security context holder
        SecurityContextHolder.clearContext();
    }

    @Override
    public JwtAuthResponse refreshAccessToken(String refreshToken) {
        String username = jwtService.extractUserName(refreshToken);

        // Find the user in the database using the username
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(UserNotFoundException::new);

        if (!jwtService.validateToken(refreshToken, userEntity)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        String newAccessToken = jwtService.generateAccessToken(userEntity);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(newAccessToken);
        jwtAuthResponse.setRefreshToken(refreshToken);
        return jwtAuthResponse;
    }
}





