package org.repin.config;

import org.repin.service.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final AuthenticationConfiguration authConfiguration;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    SecurityConfig(AuthenticationConfiguration authConfiguration,
                   JwtAuthenticationFilter jwtAuthenticationFilter){
        this.authConfiguration = authConfiguration;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()

                        //администратор -  уникальные эндпоинты
                        .requestMatchers(
                                "/api/add_faculty",
                                "/api/add_staff_member",
                                "/api/add_semester",
                                "/api/get_staff",
                                "/api/get_semesters"
                        ).hasRole("ADMIN")

                        //сотрудник деканата - уникальные эндпоинты
                        .requestMatchers(
                                "/api/add_discipline",
                                "/api/add_group",
                                "/api/add_student",
                                "/api/add_lecturer",
                                "/api/add_schedule",
                                "/api/add_headmen",
                                "/api/add_schedule_item"
                        ).hasRole("DEAN_STAFF")

                        //общие эндпоинты для нескольких ролей
                        .requestMatchers("/api/get_faculties", "/api/get_specialities").hasAnyRole("ADMIN", "DEAN_STAFF")
                        .requestMatchers("/api/get_disciplines").hasAnyRole("ADMIN", "DEAN_STAFF", "LECTURER", "HEADMAN")
                        .requestMatchers("/api/get_groups", "/api/get_report").hasAnyRole("DEAN_STAFF", "LECTURER")
                        .requestMatchers("/api/get_students_by_group").hasAnyRole("DEAN_STAFF", "LECTURER", "HEADMAN")
                        .requestMatchers("/api/get_schedules_for_faculty").hasAnyRole("DEAN_STAFF", "LECTURER")
                        .requestMatchers("/api/get_concrete_schedules_by_filters").hasAnyRole("LECTURER", "HEADMAN")
                        .requestMatchers("/api/get_attendance_for_schedule").hasAnyRole("LECTURER", "HEADMAN")
                        .requestMatchers("/api/mark_attendance").hasAnyRole("LECTURER", "HEADMAN")
                        .requestMatchers("/api/get_lecturers").hasAnyRole("DEAN_STAFF", "HEADMAN")


                        //уникальные эндпоинты для сотрудника деканата
                        .requestMatchers(
                                "/api/get_students",
                                "/api/get_headmen",
                                "/api/get_groups_by_dean_staff"
                        ).hasRole("DEAN_STAFF")


                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}