package org.repin.service.auth;

import lombok.RequiredArgsConstructor;
import org.repin.model.AppUser;
import org.repin.repository.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final DeanStaffRepository deanStaffRepository;
    private final AdminRepository adminRepository;



    public AppUser loadAppUserByUsername(String usernameWithRole) throws UsernameNotFoundException {
        UserRoleContext context = parseUsernameWithRole(usernameWithRole);
        return findUserByRoleAndEmail(context.role(), context.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Optional<? extends AppUser> findUserByRoleAndEmail(String role, String email) {
        return switch (role) {
            case "STUDENT" -> studentRepository.findByEmail(email);
            case "LECTURER" -> lecturerRepository.findByEmail(email);
            case "DEAN_STAFF" -> deanStaffRepository.findByEmail(email);
            case "ADMIN" -> adminRepository.findByEmail(email);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    private UserRoleContext parseUsernameWithRole(String usernameWithRole) {
        String[] parts = usernameWithRole.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Username must be in format 'email:role'");
        }
        return new UserRoleContext(parts[0], parts[1]);
    }

    private UserDetails buildUserDetails(AppUser appUser) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(appUser.getEmail() + ":" + appUser.getRole())
                .password(appUser.getPassword())
                .authorities(getAuthorities(appUser))
                .accountLocked(false)
                .disabled(false)
                .credentialsExpired(false)
                .accountExpired(false)
                .build();
    }

    private String[] getAuthorities(AppUser appUser) {
        return new String[] { appUser.getRole() };
    }

    private record UserRoleContext(String email, String role) {}
}