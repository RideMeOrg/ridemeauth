package backend.rideme.auth.services;

import backend.rideme.auth.dto.converters.ApiResponse;
import backend.rideme.auth.entities.MediaFile;
import backend.rideme.auth.entities.Profile;
import backend.rideme.auth.entities.tksmanager.Shift;
import backend.rideme.auth.entities.tksmanager.TaskCategory;
import backend.rideme.auth.entities.tksmanager.TaskComment;
import backend.rideme.auth.entities.tksmanager.Tasks;
import backend.rideme.auth.repositories.MediaFileRepository;
import backend.rideme.auth.repositories.ProfileRepository;
import backend.rideme.auth.repositories.tksmanager.ShiftRepository;
import backend.rideme.auth.repositories.tksmanager.TaskCategoryRepository;
import backend.rideme.auth.repositories.tksmanager.TaskCommentRepository;
import backend.rideme.auth.repositories.tksmanager.TasksRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import backend.rideme.auth.exceptions.RequestNotAcceptableException;
import backend.rideme.auth.exceptions.ResourceNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TasksServiceImpl implements TasksService {

    private final ShiftRepository shiftRepository;
    private final TaskCategoryRepository taskCategoryRepository;
    private final TasksRepository tasksRepository;
    private final ProfileRepository profileRepository;
    private final MediaFileRepository mediaFileRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final LoggerUser loggerUser;

    @PersistenceContext
    private EntityManager entityManager;


    public TasksServiceImpl(ShiftRepository shiftRepository, TaskCategoryRepository taskCategoryRepository, TasksRepository tasksRepository, ProfileRepository profileRepository, MediaFileRepository mediaFileRepository, TaskCommentRepository taskCommentRepository, LoggerUser loggerUser) {
        this.shiftRepository = shiftRepository;
        this.taskCategoryRepository = taskCategoryRepository;
        this.tasksRepository = tasksRepository;
        this.profileRepository = profileRepository;
        this.mediaFileRepository = mediaFileRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.loggerUser = loggerUser;
    }

    @Override
    public List<Shift> getAllShifts() {
        return shiftRepository.findByProfileId(loggerUser.getCurrentProfile().getId());
    }

    @Transactional
    @Override
    public Shift addShift(Shift shift) {
        if (shiftRepository.existsByBordName(shift.getBordName())) {
            throw new RequestNotAcceptableException("The name is already use");
        }
        shift.setProfile(loggerUser.getCurrentProfile());
        shift = shiftRepository.save(shift);

        List<TaskCategory> taskCategories = taskCategoryRepository.findByDefaultTaskCategory(true);
        for (TaskCategory taskCategory : taskCategories) {
            entityManager.detach(taskCategory);
            taskCategory.setId(null);
            taskCategory.setDefaultTaskCategory(false);
            taskCategory.setShift(shift);
            taskCategoryRepository.save(taskCategory);
        }
        return shift;
    }

    @Override
    public Shift getShift(UUID dashboardId) {
        return shiftRepository.findById(dashboardId).orElseThrow(() -> new ResourceNotFoundException("Shift", "id", dashboardId));
    }

    @Override
    public Shift updateShift(UUID dashboardId, Shift shift) {
        Shift shiftDb = shiftRepository.findById(dashboardId).orElseThrow(() -> new ResourceNotFoundException("Shift", "id", dashboardId));
        shiftDb.setBordName(shift.getBordName());
        shiftDb.setDescriptions(shift.getDescriptions());
        return shiftRepository.save(shiftDb);
    }

    @Override
    public ApiResponse deleteShift(UUID dashboardId) {
        Shift shiftDb = shiftRepository.findById(dashboardId).orElseThrow(() -> new ResourceNotFoundException("Shift", "id", dashboardId));
        List<TaskCategory> taskCategories = getTaskCategoryByShift(dashboardId);
        for (TaskCategory taskCategory : taskCategories) {
            deleteTaskCategory(dashboardId, taskCategory.getId());
        }
        shiftRepository.delete(shiftDb);
        return new ApiResponse(true, "Shift deleted successfully");
    }

    @Override
    public List<TaskCategory> getTaskCategoryByShift(UUID dashboardId) {
        return taskCategoryRepository.findByShiftId(dashboardId);
    }

    @Override
    public List<Tasks> getTasksByCategory(UUID taskCategoryId, String search) {
        Specification<Tasks> tasksSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("taskCategory").get("id"), taskCategoryId);
        if (search != null && !search.isEmpty()) {
            tasksSpecification = tasksSpecification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + search + "%"));
        }
        return tasksRepository.findAll(tasksSpecification);
    }

    @Override
    public TaskCategory addTaskCategory(UUID dashboardId, TaskCategory taskCategory) {
        Shift shiftDb = shiftRepository.findById(dashboardId).orElseThrow(() -> new ResourceNotFoundException("Shift", "id", dashboardId));
        taskCategory.setShift(shiftDb);
        return taskCategoryRepository.save(taskCategory);
    }

    @Override
    public TaskCategory updateTaskCategory(UUID dashboardId, UUID taskCategoryId, TaskCategory taskCategory) {
        if (!shiftRepository.existsById(dashboardId)) {
            throw new ResourceNotFoundException("Shift", "id", dashboardId);
        }
        TaskCategory taskCategoryDB = taskCategoryRepository.findById(taskCategoryId).orElseThrow(() -> new ResourceNotFoundException("TaskCategory", "id", taskCategoryId));
        taskCategoryDB.setName(taskCategory.getName());
        taskCategoryDB.setIndexColor(taskCategory.getIndexColor());
        return taskCategoryRepository.save(taskCategoryDB);
    }

    @Override
    public ApiResponse deleteTaskCategory(UUID dashboardId, UUID taskCategoryId) {

        if (!taskCategoryRepository.existsById(taskCategoryId)) {
            throw new ResourceNotFoundException("TaskCategory", "id", taskCategoryId);
        }

        if (tasksRepository.countByTaskCategoryId(taskCategoryId) > 0) {
            throw new RequestNotAcceptableException("Can not delete this task category. Please remove all task first");
        }

        taskCategoryRepository.deleteById(taskCategoryId);
        return new ApiResponse(true, "Task category is deleted successfully");
    }

    @Override
    public Tasks addTasks(UUID taskCategoryId, Tasks tasks) {
        TaskCategory taskCategoryDB = taskCategoryRepository.findById(taskCategoryId).orElseThrow(() -> new ResourceNotFoundException("TaskCategory", "id", taskCategoryId));
        tasks.setCreatedBy(loggerUser.getCurrentUser());
        tasks.setTaskCategory(taskCategoryDB);
        tasks.setShift(taskCategoryDB.getShift());
        return tasksRepository.save(tasks);
    }

    @Override
    public Tasks updateTasks(UUID taskCategoryId, UUID tasksId, Tasks tasks) {
        TaskCategory taskCategoryDB = taskCategoryRepository.findById(taskCategoryId).orElseThrow(() -> new ResourceNotFoundException("TaskCategory", "id", taskCategoryId));
        Tasks taskDb = tasksRepository.findById(tasksId).orElseThrow(() -> new ResourceNotFoundException("Tasks", "id", tasksId));

        if (taskDb.getTaskCategory().getId() != taskCategoryDB.getId()) {
            taskDb.setTaskCategory(taskCategoryDB);
        }

        taskDb.setTitle(tasks.getTitle());
        taskDb.setImageDescription(tasks.getImageDescription());
        taskDb.setDescription(tasks.getDescription());
        taskDb.setTags(tasks.getTags());
        taskDb.setBadgeColor(tasks.getBadgeColor());
        taskDb.setDeadline(tasks.getDeadline());

        return tasksRepository.save(taskDb);
    }

    @Override
    public ApiResponse deleteTasks(UUID taskCategoryId, UUID tasksId) {
        if (!taskCategoryRepository.existsById(taskCategoryId)) {
            throw new ResourceNotFoundException("TaskCategory", "id", taskCategoryId);
        }

        if (!tasksRepository.existsById(tasksId)) {
            throw new ResourceNotFoundException("Tasks", "id", tasksId);
        }

        // TODO: 8/16/2023 review after implement comment section
        tasksRepository.deleteById(tasksId);

        return new ApiResponse(true, "Tasks is deleted successfully");
    }

    @Override
    public ApiResponse addUserToTheTask(UUID taskId, UUID profileId) {
        Tasks tasks = tasksRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Tasks", "id", taskId));
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));

        if (tasks.getProfiles() == null) {
            tasks.setProfiles(new ArrayList<>());
        }

        tasks.getProfiles().add(profile);
        tasksRepository.save(tasks);
        return new ApiResponse(true, profile.getFirstName() + " " + profile.getLastName() + " is successfully add");
    }

    @Override
    public ApiResponse removeUserFromTask(UUID taskId, UUID profileId) {
        Tasks tasks = tasksRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Tasks", "id", taskId));
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));

        tasks.getProfiles().removeIf(profileI -> profileI.getId() == profileId);
        tasksRepository.save(tasks);
        return new ApiResponse(true, profile.getFirstName() + " " + profile.getLastName() + " is successfully remove");
    }

    @Override
    public ApiResponse attachFileToTheTask(UUID taskId, long mediaFileId) {
        Tasks tasks = tasksRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Tasks", "id", taskId));
        MediaFile mediaFile = mediaFileRepository.findById(mediaFileId).orElseThrow(() -> new ResourceNotFoundException("MediaFile", "id", mediaFileId));

        if (tasks.getMediaFiles() == null) {
            tasks.setMediaFiles(new ArrayList<>());
        }
        tasks.getMediaFiles().add(mediaFile);
        tasksRepository.save(tasks);
        return new ApiResponse(true, mediaFile.getOriginalName() + " is successfully add");
    }

    @Override
    public ApiResponse removeFileFromTask(UUID taskId, long mediaFileId) {
        Tasks tasks = tasksRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Tasks", "id", taskId));
        MediaFile mediaFile = mediaFileRepository.findById(mediaFileId).orElseThrow(() -> new ResourceNotFoundException("MediaFile", "id", mediaFileId));

        tasks.getMediaFiles().removeIf(mediaFile1 -> mediaFile1.getId() == mediaFileId);
        tasksRepository.save(tasks);
        return new ApiResponse(true, mediaFile.getOriginalName() + " is successfully remove");
    }

    @Override
    public TaskComment addCommentToTheTasks(UUID taskId, TaskComment taskComment) {
        Tasks tasks = tasksRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Tasks", "id", taskId));

        if (tasks.getTaskComments() == null) {
            tasks.setTaskComments(new ArrayList<>());
        }

        taskComment.setProfile(loggerUser.getCurrentProfile());
        taskComment.setTasks(tasks);
        taskComment = taskCommentRepository.save(taskComment);
        tasks.getTaskComments().add(taskComment);
        tasksRepository.save(tasks);
        return taskComment;
    }

    @Override
    public ApiResponse removeCommentFromTask(UUID taskId, long taskCommentId) {
        Tasks tasks = tasksRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Tasks", "id", taskId));
        TaskComment taskComment = taskCommentRepository.findById(taskCommentId).orElseThrow(() -> new ResourceNotFoundException("TaskComment", "id", taskCommentId));

        tasks.getTaskComments().removeIf(taskComment1 -> taskComment1.getId() == taskCommentId);
        tasksRepository.save(tasks);
        return new ApiResponse(true, "Comment is successfully remove");
    }
}
