package backend.rideme.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.rideme.auth.entities.Privilege;
import backend.rideme.auth.entities.enums.TypePrivilege;
import backend.rideme.auth.entities.enums.TypeRole;

import java.util.List;


public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(TypePrivilege privilege);

    List<Privilege> findByNameNotIn(TypePrivilege[] typePrivileges);

    boolean existsByName(TypePrivilege privilege);

    List<Privilege> findByTypeRole(TypeRole typeRole);
}
