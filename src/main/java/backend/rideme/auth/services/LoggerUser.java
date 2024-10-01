package backend.rideme.auth.services;


import backend.rideme.auth.entities.Profile;
import backend.rideme.auth.entities.User;
import backend.rideme.auth.repositories.ProfileRepository;
import backend.rideme.auth.repositories.UserRepository;
import backend.rideme.auth.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import backend.rideme.auth.exceptions.ResourceNotFoundException;

@Component
public class LoggerUser {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public LoggerUser(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }


    public Profile getCurrentProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) return null;
        User user = userRepository.getReferenceById(((UserPrincipal) auth.getPrincipal()).getId());
        return profileRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Profile", "current user", user.getId()));
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) return null;
        return userRepository.getReferenceById(((UserPrincipal) auth.getPrincipal()).getId());
    }

    public boolean checkAuthorization(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) return false;
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().equals(permission))
                return true;
        }
        return false;
    }
}
