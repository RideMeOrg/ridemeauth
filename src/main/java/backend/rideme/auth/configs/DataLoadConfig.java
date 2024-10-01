package backend.rideme.auth.configs;

import backend.rideme.auth.entities.Privilege;
import backend.rideme.auth.entities.Profile;
import backend.rideme.auth.entities.Role;
import backend.rideme.auth.entities.User;
import backend.rideme.auth.repositories.PrivilegeRepository;
import backend.rideme.auth.repositories.ProfileRepository;
import backend.rideme.auth.repositories.RoleRepository;
import backend.rideme.auth.repositories.UserRepository;
import backend.rideme.auth.repositories.tksmanager.TaskCategoryRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import backend.rideme.auth.entities.enums.StatusUser;
import backend.rideme.auth.entities.enums.TypePrivilege;
import backend.rideme.auth.entities.enums.TypeRole;
import backend.rideme.auth.entities.tksmanager.TaskCategory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class DataLoadConfig {
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    public DataLoadConfig(RoleRepository roleRepository, PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder,
                          UserRepository userRepository, ProfileRepository profileRepository, TaskCategoryRepository taskCategoryRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    @PostConstruct
    public void loadData() {
        loadRole();
        loadDefaultTaskCategories();
    }

    public void loadRole() {
        if (roleRepository.existsByType(TypeRole.SUPER_ADMIN))
            return;

        List<Privilege> privilegeAdmin = new ArrayList<>();
        for (int i = 1; i < TypePrivilege.values().length; i++) {
            String description = TypePrivilege.values()[i].toString().replace("_", " ");
            String category = description.split(" ")[description.split(" ").length - 1];
            privilegeAdmin.add(privilegeRepository.save(new Privilege(category, TypePrivilege.values()[i], description, TypeRole.SUPER_ADMIN)));
        }

        List<Privilege> privilegeUser = new ArrayList<>();
        for (int i = 11; i < TypePrivilege.values().length; i++) {
            String description = TypePrivilege.values()[i].toString().replace("_", " ");
            String category = description.split(" ")[description.split(" ").length - 1];
            privilegeUser.add(privilegeRepository.save(new Privilege(category, TypePrivilege.values()[i], description, TypeRole.USER)));
        }

        Role roleUser = new Role("USER", TypeRole.USER);
        roleUser.setPrivileges(privilegeUser);
        roleRepository.save(roleUser);


        Role roleAdmin = new Role("Super Admin", TypeRole.SUPER_ADMIN);
        roleAdmin.setPrivileges(privilegeAdmin);
        roleAdmin = roleRepository.save(roleAdmin);

        User admin = new User();
        admin.setActive(true);
        admin.setEmail("lazare.kounasso@esmt.sn");
        admin.setName("Super Administrateur");
        admin.setUsername("lazare.kounasso@esmt.sn");
        admin.setStatus(StatusUser.ACTIVE);
        admin.setPassword(passwordEncoder.encode("mot2P@ss"));
        admin.setRoles(Collections.singletonList(roleAdmin));
        admin = userRepository.save(admin);

        Profile profile = new Profile();
        profile.setEmail("lazare.kounasso@esmt.sn");
        profile.setFirstName("Administrateur");
        profile.setLastName("Super");
        profile.setGender("M");
        profile.setPhone("+221785900131");
        profile.setUser(admin);
        profile.setUsername("lazare.kounasso@esmt.sn");
        profileRepository.save(profile);
    }

    private void loadDefaultTaskCategories() {
        if (taskCategoryRepository.count() > 0) {
            return;
        }

        List<TaskCategory> taskCategories = new ArrayList<>();

        TaskCategory taskCategory1 = new TaskCategory();
        taskCategory1.setDefaultTaskCategory(true);
        taskCategory1.setName("Backlog");
        taskCategory1.setIndexColor("#E9E9E9");
        taskCategories.add(taskCategory1);

        TaskCategory taskCategory2 = new TaskCategory();
        taskCategory2.setDefaultTaskCategory(true);
        taskCategory2.setName("In Progress");
        taskCategory2.setIndexColor("#6A51FF");
        taskCategories.add(taskCategory2);


        TaskCategory taskCategory3 = new TaskCategory();
        taskCategory3.setDefaultTaskCategory(true);
        taskCategory3.setName("Review");
        taskCategory3.setIndexColor("#E7492E");
        taskCategories.add(taskCategory3);


        TaskCategory taskCategory4 = new TaskCategory();
        taskCategory4.setDefaultTaskCategory(true);
        taskCategory4.setName("Completed");
        taskCategory4.setIndexColor("#33AA44");
        taskCategories.add(taskCategory4);

        taskCategoryRepository.saveAll(taskCategories);
    }

}
