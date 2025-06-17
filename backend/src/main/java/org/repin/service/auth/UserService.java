package org.repin.service.auth;

import lombok.RequiredArgsConstructor;
import org.repin.dto.auth.CustomUserDetails;
import org.repin.model.AppUser;
import org.repin.repository.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final DeanStaffRepository deanStaffRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserRoleContext context = parseUsernameWithRole(username);
        AppUser user = findUserByRoleAndEmail(context.role(), context.email())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        return buildUserDetails(user);
    }


    private Optional<? extends AppUser> findUserByRoleAndEmail(String role, String email) {
        return switch (role) {
            case "HEADMAN" -> studentRepository.findByEmail(email);
            case "LECTURER" -> lecturerRepository.findByEmail(email);
            case "DEAN_STAFF" -> deanStaffRepository.findByEmail(email);
            case "ADMIN" -> adminRepository.findByEmail(email);
            default -> throw new IllegalArgumentException();
        };
    }

    private UserRoleContext parseUsernameWithRole(String usernameWithRole) {
        String[] parts = usernameWithRole.split(":");
        return new UserRoleContext(parts[0], parts[1]);
    }

    private UserDetails buildUserDetails(AppUser appUser) {
        return new CustomUserDetails(
                appUser.getEmail() + ":" + appUser.getRole(),
                appUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + appUser.getRole())),
                appUser.getId()
        );
    }

    private String[] getAuthorities(AppUser appUser) {
        return new String[]{appUser.getRole()};
    }


    private record UserRoleContext(String email, String role){}

}