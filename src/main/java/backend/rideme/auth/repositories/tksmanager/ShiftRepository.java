package backend.rideme.auth.repositories.tksmanager;

import backend.rideme.auth.entities.tksmanager.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findByProfileId(UUID profileId);

    boolean existsByBordName(String boardName);
}
