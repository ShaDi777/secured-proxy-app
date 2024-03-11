package ru.shadi777.proxyapplication.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.enums.RoleType;
import ru.shadi777.proxyapplication.models.Privilege;
import ru.shadi777.proxyapplication.models.Role;
import ru.shadi777.proxyapplication.models.RolePrivilege;
import ru.shadi777.proxyapplication.models.User;
import ru.shadi777.proxyapplication.repositories.PrivilegeRepository;
import ru.shadi777.proxyapplication.repositories.RolePrivilegeRepository;
import ru.shadi777.proxyapplication.repositories.RoleRepository;
import ru.shadi777.proxyapplication.repositories.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final PasswordEncoder passwordEncoder;

    boolean isCompletedSetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (isCompletedSetup)
            return;

        var adminEdit = createRolePrivilegeIfNotFound(RoleType.ADMIN.name(), PrivilegeType.EDIT.name());
        var adminView = createRolePrivilegeIfNotFound(RoleType.ADMIN.name(), PrivilegeType.VIEW.name());

        createRolePrivilegeIfNotFound(RoleType.POSTS.name(), PrivilegeType.EDIT.name());
        createRolePrivilegeIfNotFound(RoleType.POSTS.name(), PrivilegeType.VIEW.name());

        createRolePrivilegeIfNotFound(RoleType.USERS.name(), PrivilegeType.EDIT.name());
        createRolePrivilegeIfNotFound(RoleType.USERS.name(), PrivilegeType.VIEW.name());

        createRolePrivilegeIfNotFound(RoleType.ALBUMS.name(), PrivilegeType.EDIT.name());
        createRolePrivilegeIfNotFound(RoleType.ALBUMS.name(), PrivilegeType.VIEW.name());


        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRolePrivilegeCollection(List.of(adminView, adminEdit));
        user.setEnabled(true);
        user.setLocked(false);

        createUserIfNotFound(user);

        isCompletedSetup = true;
    }

    @Transactional
    User createUserIfNotFound(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser == null) {
            userRepository.save(user);
        }

        return user;
    }

    @Transactional
    RolePrivilege createRolePrivilegeIfNotFound(String roleName, String privilegeName) {
        Role role = createRoleIfNotFound(roleName);
        Privilege privilege = createPrivilegeIfNotFound(privilegeName);

        RolePrivilege rolePrivilege = rolePrivilegeRepository.findByRoleAndPrivilege(role, privilege);
        if (rolePrivilege == null) {
            rolePrivilege = new RolePrivilege(role, privilege);
            rolePrivilegeRepository.save(rolePrivilege);
        }

        return rolePrivilege;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }

        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }

        return role;
    }
}