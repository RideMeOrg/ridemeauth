package backend.rideme.auth.repositories.tksmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.rideme.auth.entities.tksmanager.TaskComment;


public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
}
