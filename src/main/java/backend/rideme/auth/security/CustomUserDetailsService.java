package backend.rideme.auth.security;

import backend.rideme.auth.entities.User;
import backend.rideme.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import backend.rideme.auth.exceptions.ResourceNotFoundException;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        User user = userRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + username)
                );
        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(String id) throws ResourceNotFoundException {
        // TODO Auto-generated method stub

        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return UserPrincipal.create(user);
    }

}
