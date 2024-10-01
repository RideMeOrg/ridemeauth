package backend.rideme.auth.repositories.tksmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.rideme.auth.entities.tksmanager.Dashboard;

import java.util.List;
import java.util.UUID;

public interface DashboardRepository extends JpaRepository<Dashboard, UUID> {
    List<Dashboard> findByProfileId(UUID profileId);

    boolean existsByBordName(String boardName);
}
