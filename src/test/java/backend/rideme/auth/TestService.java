package backend.rideme.auth;

import backend.rideme.auth.dto.converters.ApiResponse;
import backend.rideme.auth.dto.converters.LoginRequest;
import backend.rideme.auth.entities.tksmanager.Shift;
import backend.rideme.auth.entities.tksmanager.TaskCategory;
import backend.rideme.auth.entities.tksmanager.Tasks;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import backend.rideme.auth.services.AuthService;
import backend.rideme.auth.services.TasksService;

import java.util.Date;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
public class TestService {
    @Autowired
    private TasksService tasksService;
    @Autowired
    private AuthService authService;

    @Value("${default.login.username}")
    private String username;
    @Value("${default.login.password}")
    private String password;


    @BeforeEach
    public void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(password);
        loginRequest.setUsername(username);
        authService.authenticateUser(loginRequest);
    }

    @Test
    public void getUserShift() {
        List<Shift> shiftList = this.tasksService.getAllShifts();
        Assertions.assertThat(shiftList.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void addShift() {
        Shift shift = new Shift();
        shift.setBordName("Spring boot application");
        shift.setDescriptions("Some description");
        Shift shiftB = tasksService.addShift(shift);

        Assertions.assertThat(shiftB).isNotNull();
        Assertions.assertThat(shiftB.getId()).isNotNull();
        Assertions.assertThat(shiftB.getBordName()).isEqualTo(shift.getBordName());
        Assertions.assertThat(shiftB.getDescriptions()).isEqualTo(shift.getDescriptions());

        List<TaskCategory> taskCategories = tasksService.getTaskCategoryByShift(shift.getId());
        Assertions.assertThat(taskCategories.size()).isGreaterThan(0);
    }

    @Test
    public void updateShift() {
        Shift shiftB = new Shift();
        shiftB.setBordName("Financial App");
        shiftB.setDescriptions("Some description");
        shiftB = tasksService.addShift(shiftB);

        Assertions.assertThat(shiftB.getId()).isNotNull();

        shiftB.setBordName(shiftB.getBordName() + "Update");

        Shift shiftDB = this.tasksService.updateShift(shiftB.getId(), shiftB);
        Assertions.assertThat(shiftDB.getBordName()).isEqualTo(shiftB.getBordName());
    }

    @Test
    public void deleteShift() {
        Shift shift = new Shift();
        shift.setBordName("Docker App");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        ApiResponse apiResponse = this.tasksService.deleteShift(shift.getId());
        Assertions.assertThat(apiResponse).isNotNull();
        Assertions.assertThat(apiResponse.getSuccess()).isEqualTo(true);
    }

    @Test
    public void getTaskCategoryByShift() {
        Shift shift = new Shift();
        shift.setBordName("Docker App 2");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setShift(shift);
        taskCategory.setName("TaskCategory");
        taskCategory.setIndexColor("#000000");

        taskCategory = tasksService.addTaskCategory(shift.getId(), taskCategory);

        Assertions.assertThat(taskCategory.getId()).isNotNull();
        Assertions.assertThat(taskCategory.isDefaultTaskCategory()).isEqualTo(false);
        Assertions.assertThat(taskCategory.getShift().getId()).isEqualTo(shift.getId());


        List<TaskCategory> taskCategories = tasksService.getTaskCategoryByShift(shift.getId());
        Assertions.assertThat(taskCategories.size()).isGreaterThan(0);
    }

    @Test
    public void getTasksByCategory() {
        Shift shift = new Shift();
        shift.setBordName("Docker App 3");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setShift(shift);
        taskCategory.setName("TaskCategory");
        taskCategory.setIndexColor("#000000");

        taskCategory = tasksService.addTaskCategory(shift.getId(), taskCategory);

        Assertions.assertThat(taskCategory.getId()).isNotNull();

        Tasks tasks = new Tasks();
        tasks.setTitle("My tasks");
        tasks.setTags(List.of("Design", "UI/UX"));
        tasks.setBadgeColor(List.of("#000000", "#ffffff"));
        tasks.setDeadline(new Date());
        tasks.setShift(shift);
        tasks.setTaskCategory(taskCategory);

        tasks = tasksService.addTasks(taskCategory.getId(), tasks);

        Assertions.assertThat(tasks.getId()).isNotNull();
        Assertions.assertThat(tasks.getCreatedBy()).isNotNull();

        List<Tasks> tasksList = tasksService.getTasksByCategory(taskCategory.getId(), null);
        Assertions.assertThat(tasksList.size()).isEqualTo(1);

        Tasks tasks2 = new Tasks();
        tasks2.setTitle("My Apps Tasks");
        tasks2.setTags(List.of("Design", "UI/UX"));
        tasks2.setBadgeColor(List.of("#000000", "#ffffff"));
        tasks2.setDeadline(new Date());
        tasks2.setShift(shift);
        tasks2.setTaskCategory(taskCategory);

        tasks2 = tasksService.addTasks(taskCategory.getId(), tasks2);

        Assertions.assertThat(tasks2.getId()).isNotNull();
        Assertions.assertThat(tasks2.getCreatedBy()).isNotNull();

        List<Tasks> tasksList2 = tasksService.getTasksByCategory(taskCategory.getId(), "My Apps Tasks");
        Assertions.assertThat(tasksList2.size()).isEqualTo(1);


        List<Tasks> tasksList3 = tasksService.getTasksByCategory(taskCategory.getId(), "");
        Assertions.assertThat(tasksList3.size()).isEqualTo(2);

    }

    @Test
    public void addTaskCategory() {
        Shift shift = new Shift();
        shift.setBordName("Add tasks category dash");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setShift(shift);
        taskCategory.setName("TaskCategory");
        taskCategory.setIndexColor("#000000");

        taskCategory = tasksService.addTaskCategory(shift.getId(), taskCategory);
        Assertions.assertThat(taskCategory.getId()).isNotNull();
        Assertions.assertThat(taskCategory.getName()).isEqualTo("TaskCategory");
    }

    @Test
    public void updateTaskCategory() {
        Shift shift = new Shift();
        shift.setBordName("Update tasks category dash");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setShift(shift);
        taskCategory.setName("TaskCategory");
        taskCategory.setIndexColor("#000000");

        taskCategory = tasksService.addTaskCategory(shift.getId(), taskCategory);
        Assertions.assertThat(taskCategory.getId()).isNotNull();

        taskCategory.setName("Update Task");
        taskCategory = tasksService.updateTaskCategory(shift.getId(), taskCategory.getId(), taskCategory);

        Assertions.assertThat(taskCategory.getId()).isNotNull();
        Assertions.assertThat(taskCategory.getName()).isEqualTo("Update Task");
    }

    @Test
    public void deleteTaskCategory() {
        Shift shift = new Shift();
        shift.setBordName("Delete tasks dash");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setShift(shift);
        taskCategory.setName("TaskCategory");
        taskCategory.setIndexColor("#000000");

        taskCategory = tasksService.addTaskCategory(shift.getId(), taskCategory);
        Assertions.assertThat(taskCategory.getId()).isNotNull();

        ApiResponse apiResponse = tasksService.deleteTaskCategory(shift.getId(), taskCategory.getId());
        Assertions.assertThat(apiResponse.getSuccess()).isEqualTo(true);

    }

    @Test
    public void addTasks() {
        Shift shift = new Shift();
        shift.setBordName("Add tasks dash");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setShift(shift);
        taskCategory.setName("TaskCategory");
        taskCategory.setIndexColor("#000000");

        taskCategory = tasksService.addTaskCategory(shift.getId(), taskCategory);

        Assertions.assertThat(taskCategory.getId()).isNotNull();

        Tasks tasks = new Tasks();
        tasks.setTitle("My tasks");
        tasks.setTags(List.of("Design", "UI/UX"));
        tasks.setBadgeColor(List.of("#000000", "#ffffff"));
        tasks.setDeadline(new Date());
        tasks.setShift(shift);
        tasks.setTaskCategory(taskCategory);

        tasks = tasksService.addTasks(taskCategory.getId(), tasks);

        Assertions.assertThat(tasks.getId()).isNotNull();
        Assertions.assertThat(tasks.getCreatedBy()).isNotNull();
    }

    @Test
    public void updateTasks() {
        Shift shift = new Shift();
        shift.setBordName("Update tasks dash");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setShift(shift);
        taskCategory.setName("TaskCategory");
        taskCategory.setIndexColor("#000000");

        taskCategory = tasksService.addTaskCategory(shift.getId(), taskCategory);

        Assertions.assertThat(taskCategory.getId()).isNotNull();

        Tasks tasks = new Tasks();
        tasks.setTitle("My tasks");
        tasks.setTags(List.of("Design", "UI/UX"));
        tasks.setBadgeColor(List.of("#000000", "#ffffff"));
        tasks.setDeadline(new Date());
        tasks.setShift(shift);
        tasks.setTaskCategory(taskCategory);

        tasks = tasksService.addTasks(taskCategory.getId(), tasks);

        Assertions.assertThat(tasks.getId()).isNotNull();
        Assertions.assertThat(tasks.getCreatedBy()).isNotNull();

        tasks.setTitle("Update task");

        tasks = tasksService.updateTasks(taskCategory.getId(), tasks.getId(), tasks);
        Assertions.assertThat(tasks.getTitle()).isEqualTo("Update task");


        TaskCategory taskCategory2 = new TaskCategory();
        taskCategory2.setShift(shift);
        taskCategory2.setName("TaskCategory2");
        taskCategory2.setIndexColor("#ffffff");

        taskCategory2 = tasksService.addTaskCategory(shift.getId(), taskCategory2);

        Assertions.assertThat(taskCategory2.getId()).isNotNull();


        tasks = tasksService.updateTasks(taskCategory2.getId(), tasks.getId(), tasks);
        Assertions.assertThat(tasks.getTaskCategory().getId()).isNotNull();
        Assertions.assertThat(tasks.getTaskCategory().getId()).isEqualTo(taskCategory2.getId());

    }

    @Test
    public void deleteTasks() {
        Shift shift = new Shift();
        shift.setBordName("delete tasks dash");
        shift.setDescriptions("Some description");
        shift = tasksService.addShift(shift);
        Assertions.assertThat(shift.getId()).isNotNull();

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setShift(shift);
        taskCategory.setName("TaskCategory");
        taskCategory.setIndexColor("#000000");

        taskCategory = tasksService.addTaskCategory(shift.getId(), taskCategory);

        Assertions.assertThat(taskCategory.getId()).isNotNull();

        Tasks tasks = new Tasks();
        tasks.setTitle("My tasks");
        tasks.setTags(List.of("Design", "UI/UX"));
        tasks.setBadgeColor(List.of("#000000", "#ffffff"));
        tasks.setDeadline(new Date());
        tasks.setShift(shift);
        tasks.setTaskCategory(taskCategory);

        tasks = tasksService.addTasks(taskCategory.getId(), tasks);

        Assertions.assertThat(tasks.getId()).isNotNull();
        Assertions.assertThat(tasks.getCreatedBy()).isNotNull();

        ApiResponse apiResponse = tasksService.deleteTasks(taskCategory.getId(), tasks.getId());

        Assertions.assertThat(apiResponse.getSuccess()).isEqualTo(true);
    }

    @Test
    public void addUserToTheTask() {
        // TODO: 8/16/2023 addUserToTheTask test
    }

    @Test
    public void removeUserFromTask() {
        // TODO: 8/16/2023 removeUserFromTask test

    }

    @Test
    public void attachFileToTheTask() {
        // TODO: 8/16/2023 attachFileToTheTask test
    }

    @Test
    public void removeFileFromTask() {
        // TODO: 8/16/2023 removeFileFromTask test

    }
}
