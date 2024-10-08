package backend.rideme.auth.services;

import backend.rideme.auth.dto.converters.ApiResponse;
import backend.rideme.auth.entities.tksmanager.Shift;
import backend.rideme.auth.entities.tksmanager.TaskCategory;
import backend.rideme.auth.entities.tksmanager.TaskComment;
import backend.rideme.auth.entities.tksmanager.Tasks;

import java.util.List;
import java.util.UUID;

public interface TasksService {
    List<Shift> getAllShifts();

    Shift addShift(Shift shift);
    Shift getShift(UUID dashboardId);

    Shift updateShift(UUID dashboardId, Shift shift);

    ApiResponse deleteShift(UUID dashboardId);

    List<TaskCategory> getTaskCategoryByShift(UUID dashboardId);

    List<Tasks> getTasksByCategory(UUID taskCategoryId, String search);

    TaskCategory addTaskCategory(UUID dashboardId, TaskCategory taskCategory);

    TaskCategory updateTaskCategory(UUID dashboardId, UUID taskCategoryId, TaskCategory taskCategory);

    ApiResponse deleteTaskCategory(UUID dashboardId, UUID taskCategoryId);

    Tasks addTasks(UUID taskCategoryId, Tasks tasks);

    Tasks updateTasks(UUID taskCategoryId, UUID tasksId, Tasks tasks);

    ApiResponse deleteTasks(UUID taskCategoryId, UUID tasksId);

    ApiResponse addUserToTheTask(UUID taskId, UUID profileId);

    ApiResponse removeUserFromTask(UUID taskId, UUID profileId);

    ApiResponse attachFileToTheTask(UUID taskId, long mediaFileId);

    ApiResponse removeFileFromTask(UUID taskId, long mediaFileId);


    TaskComment addCommentToTheTasks(UUID taskId, TaskComment taskComment);

    ApiResponse removeCommentFromTask(UUID taskId, long taskCommentId);
}
