package backend.rideme.auth.controllers;

import backend.rideme.auth.entities.tksmanager.Shift;
import org.springframework.web.bind.annotation.*;
import backend.rideme.auth.dto.converters.ApiResponse;
import backend.rideme.auth.services.TasksService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/ridemeauth")
public class ShiftController {
    private final TasksService tasksService;

    public ShiftController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping("shift")
    public List<Shift> getAllShifts() {
        return this.tasksService.getAllShifts();
    }

    @PostMapping("shift")
    public Shift addShift(@RequestBody @Valid Shift shift) {
        return this.tasksService.addShift(shift);
    }

    @GetMapping("shift/{shiftId}")
    public Shift getShift(@PathVariable UUID dashboardId) {
        return this.tasksService.getShift(dashboardId);
    }

    @PutMapping("shift/{shiftId}")
    public Shift updateShift(@PathVariable UUID dashboardId, @RequestBody @Valid Shift shift) {
        return this.tasksService.updateShift(dashboardId, shift);
    }

    @DeleteMapping("shift/{shiftId}")
    public ApiResponse deleteShift(@PathVariable UUID dashboardId) {
        return this.tasksService.deleteShift(dashboardId);
    }

//    @GetMapping("dashboard/{dashboardId}/task-categories")
//    public List<TaskCategory> getTaskCategoryByShift(@PathVariable UUID dashboardId) {
//        return this.tasksService.getTaskCategoryByShift(dashboardId);
//    }
//
//    @GetMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks")
//    public List<Tasks> getTasksByCategory(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @RequestParam(defaultValue = "", required = false) String search) {
//        return this.tasksService.getTasksByCategory(taskCategoryId, search);
//    }
//
//    @PostMapping("dashboard/{dashboardId}/task-categories")
//    public TaskCategory addTaskCategory(@PathVariable UUID dashboardId, @RequestBody @Valid TaskCategory taskCategory) {
//        return this.tasksService.addTaskCategory(dashboardId, taskCategory);
//    }
//
//    @PutMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}")
//    public TaskCategory updateTaskCategory(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @RequestBody @Valid TaskCategory taskCategory) {
//        return this.tasksService.updateTaskCategory(dashboardId, taskCategoryId, taskCategory);
//    }
//
//    @DeleteMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}")
//    public ApiResponse deleteTaskCategory(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId) {
//        return this.tasksService.deleteTaskCategory(dashboardId, taskCategoryId);
//    }
//
//    @PostMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks")
//    public Tasks addTasks(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @RequestBody @Valid Tasks tasks) {
//        return this.tasksService.addTasks(taskCategoryId, tasks);
//    }
//
//    @PutMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks/{tasksId}")
//    public Tasks updateTasks(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @PathVariable UUID tasksId, @RequestBody @Valid Tasks tasks) {
//        return this.tasksService.updateTasks(taskCategoryId, tasksId, tasks);
//    }
//
//    @DeleteMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks/{tasksId}")
//    public ApiResponse deleteTasks(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @PathVariable UUID tasksId) {
//        return this.tasksService.deleteTasks(taskCategoryId, tasksId);
//    }
//
//    @PutMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks/{tasksId}/add-user")
//    public ApiResponse addUserToTheTask(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @PathVariable UUID tasksId, @RequestParam UUID profileId) {
//        return this.tasksService.addUserToTheTask(taskCategoryId, profileId);
//    }
//
//    @DeleteMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks/{tasksId}/add-user")
//    public ApiResponse removeUserFromTask(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @PathVariable UUID tasksId, @RequestParam UUID profileId) {
//        return this.tasksService.removeUserFromTask(tasksId, profileId);
//    }
//
//
//    @PutMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks/{tasksId}/attach-file")
//    public ApiResponse attachFileToTheTask(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @PathVariable UUID tasksId, @RequestParam long mediaFileId) {
//        return this.tasksService.attachFileToTheTask(tasksId, mediaFileId);
//    }
//
//    @DeleteMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks/{tasksId}/attach-file")
//    public ApiResponse removeFileFromTask(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @PathVariable UUID tasksId, @RequestParam long mediaFileId) {
//        return this.tasksService.removeFileFromTask(tasksId, mediaFileId);
//    }
//
//
//    @PostMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks/{tasksId}/comment")
//    public TaskComment addCommentToTheTasks(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @PathVariable UUID tasksId, @RequestBody TaskComment taskComment) {
//        return this.tasksService.addCommentToTheTasks(tasksId, taskComment);
//    }
//
//    @DeleteMapping("dashboard/{dashboardId}/task-categories/{taskCategoryId}/tasks/{tasksId}/comment")
//    public ApiResponse removeCommentFromTask(@PathVariable UUID dashboardId, @PathVariable UUID taskCategoryId, @PathVariable UUID tasksId, @RequestParam long taskCommentId) {
//        return this.tasksService.removeCommentFromTask(tasksId, taskCommentId);
//    }
}
