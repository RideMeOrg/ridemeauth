package backend.rideme.auth.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import backend.rideme.auth.entities.Profile;
import backend.rideme.auth.entities.User;
import backend.rideme.auth.entities.enums.StatusUser;
import backend.rideme.auth.entities.enums.TypeRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    //	Profile findByUserId(UUID id);
    Optional<Profile> findByUser(User user);

    Optional<Profile> findByUserId(UUID userId);

    Page<Profile> findByUserRoles_id(int id, Pageable page);

    Page<Profile> findByUser_Roles_IdNot(int id, Pageable page);

    Page<Profile> findByUser_Roles_Type(TypeRole type, Pageable page);

    List<Profile> findByUser_Roles_TypeAndUserStatus(TypeRole type, StatusUser status);

    Page<Profile> findByFirstNameLikeOrLastNameLike(String keyword1, String keyword2, Pageable page);

    Page<Profile> findByFirstNameLikeOrLastNameLikeAndUserRoles_id(String keyword1, String keyword2, int id, Pageable page);

    Page<Profile> findByFirstNameLikeOrLastNameLikeAndUserRoles_idIn(String keyword1, String keyword2, int[] id, Pageable page);


}
