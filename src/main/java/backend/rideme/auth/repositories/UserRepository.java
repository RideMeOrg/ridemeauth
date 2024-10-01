package backend.rideme.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.rideme.auth.entities.User;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailOrUsername(String email, String username);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndActive(String email, boolean active);

    Boolean existsByPhoneNumber(String phone);
}
