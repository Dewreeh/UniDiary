package org.repin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.repin.dto.auth.JwtAuthResponse;
import org.repin.dto.auth.LoginRequest;
import org.repin.model.AppUser;
import org.repin.service.auth.JwtTokenProvider;
import org.repin.service.auth.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Попытка входа для email: {}, роль: {}",
                loginRequest.getEmail(),
                loginRequest.getRole());

        String usernameWithRole = loginRequest.getEmail() + ":" + loginRequest.getRole().toUpperCase();
        log.debug("Сформированный логин с ролью: {}", usernameWithRole);

        try {
            log.debug("Попытка аутентификации...");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usernameWithRole,
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Аутентификация успешна для пользователя: {}", usernameWithRole);

            // Получение данных пользователя
            log.debug("Загрузка данных пользователя...");
            AppUser appUser = userService.loadAppUserByUsername(usernameWithRole);
            log.debug("Загружены данные пользователя: ID={}, Роль={}", appUser.getId(), appUser.getRole());

            // Генерация JWT токена
            log.debug("Генерация JWT токена...");
            String token = tokenProvider.generateToken(authentication);
            long expiresIn = tokenProvider.getExpirationInSeconds();
            log.debug("Токен сгенерирован, срок действия: {} секунд", expiresIn);

            // Формирование ответа
            JwtAuthResponse response = new JwtAuthResponse(
                    token,
                    "Bearer",
                    expiresIn,
                    appUser.getRole(),
                    appUser.getEmail(),
                    appUser.getId()
            );

            log.info("Успешный вход для пользователя: {}", appUser.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("ОШИБКА АУТЕНТИФИКАЦИИ для {}: {}", usernameWithRole, e.getMessage());
            throw e;
        }
    }
}