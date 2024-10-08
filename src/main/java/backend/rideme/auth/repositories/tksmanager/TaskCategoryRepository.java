package backend.rideme.auth.repositories.tksmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.rideme.auth.entities.tksmanager.TaskCategory;

import java.util.List;
import java.util.UUID;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, UUID> {
    List<TaskCategory> findByShiftId(UUID dashboardId);

    List<TaskCategory> findByDefaultTaskCategory(boolean value);
}
