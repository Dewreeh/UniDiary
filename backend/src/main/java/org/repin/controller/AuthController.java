package org.repin.controller;

import jakarta.validation.Valid;
import org.repin.dto.auth.CustomUserDetails;
import org.repin.dto.auth.JwtAuthResponse;
import org.repin.dto.auth.LoginRequest;
import org.repin.service.auth.JwtTokenProvider;
import org.repin.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Qualifier("authenticationManager")
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    AuthController(AuthenticationManager authenticationManager,
                   JwtTokenProvider tokenProvider,
                   UserService userService){
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Попытка входа для email: {}, роль: {}",
                loginRequest.getEmail(),
                loginRequest.getRole());

        String usernameWithRole = loginRequest.getEmail() + ":" + loginRequest.getRole().toUpperCase();
        log.info("Сформированный логин с ролью: {}", usernameWithRole);
        try {
            log.info("Попытка аутентификации...");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usernameWithRole,
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Аутентификация успешна для пользователя: {}", usernameWithRole);

            log.debug("Загрузка данных пользователя...");
            CustomUserDetails user = (CustomUserDetails) userService.loadUserByUsername(usernameWithRole);
            UUID userId = user.getUserId();
            log.debug("Загружены данные пользователя: ID={}, Роль={}", userId, user.getAuthorities());

            log.debug("Генерация JWT токена...");
            String token = tokenProvider.generateToken(authentication);
            long expiresIn = tokenProvider.getExpirationInSeconds();
            log.debug("Токен сгенерирован, срок действия: {} секунд", expiresIn);

            JwtAuthResponse response = new JwtAuthResponse(
                    token,
                    expiresIn,
                    user.getAuthorities(),
                    user.getUsername(),
                    userId
            );

            log.info("Успешный вход для пользователя: {}", user.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("ОШИБКА АУТЕНТИФИКАЦИИ для {}: {}", usernameWithRole, e.getMessage());
            throw new BadCredentialsException("Неправильный логин или пароль");
        }
    }
}