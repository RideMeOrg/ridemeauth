package backend.rideme.auth.repositories.tksmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import backend.rideme.auth.entities.tksmanager.Tasks;

import java.util.UUID;

public interface TasksRepository extends JpaRepository<Tasks, UUID>, JpaSpecificationExecutor<Tasks> {
    long countByTaskCategoryId(UUID taskCategoryId);
}
