package ru.shadi777.proxyapplication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import ru.shadi777.proxyapplication.models.Privilege;
import ru.shadi777.proxyapplication.models.Role;
import ru.shadi777.proxyapplication.models.RolePrivilege;
import ru.shadi777.proxyapplication.models.User;
import ru.shadi777.proxyapplication.repositories.PrivilegeRepository;
import ru.shadi777.proxyapplication.repositories.RolePrivilegeRepository;
import ru.shadi777.proxyapplication.repositories.RoleRepository;
import ru.shadi777.proxyapplication.repositories.UserRepository;
import ru.shadi777.proxyapplication.services.UserService;
import ru.shadi777.proxyapplication.utils.exceptions.UserExistsException;

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {IntegrationEnvironment.IntegrationEnvironmentConfig.class, JpaIntegrationTest.JpaConfig.class})
@Transactional("transactionManager")
public class JpaIntegrationTest extends IntegrationEnvironment {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final UserService userService;

    private Role adminRole;
    private Privilege editPrivilege;
    private RolePrivilege adminEditor;
    private User user;

    @Autowired
    public JpaIntegrationTest(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PrivilegeRepository privilegeRepository,
            RolePrivilegeRepository rolePrivilegeRepository,
            UserService userService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
        this.userService = userService;
    }

    @BeforeEach
    public void init() {
        adminRole = roleRepository.findByName("ADMIN");
        editPrivilege = privilegeRepository.findByName("EDIT");
        adminEditor = rolePrivilegeRepository.findByRoleAndPrivilege(adminRole, editPrivilege);

        user = new User();
        user.setUsername("user");
        user.setPassword("user");
        user.setEnabled(true);
        user.setLocked(false);
        user.setRolePrivilegeCollection(List.of(adminEditor));
    }

    @Test
    @Transactional
    @Rollback
    public void ShouldContainAdmin_WhenStarted() {
        Assertions.assertAll(
                () -> assertThat(userRepository.findByUsername("admin")).isNotNull(),
                () -> assertThat(roleRepository.findByName("ADMIN")).isNotNull(),
                () -> assertThat(privilegeRepository.findByName("VIEW")).isNotNull(),
                () -> assertThat(privilegeRepository.findByName("EDIT")).isNotNull()
        );
    }


    @Test
    @Transactional
    @Rollback
    public void ShouldContainNewUser_WhenAdded() throws UserExistsException {
        userService.registerNewUser(user);

        Assertions.assertAll(
                () -> assertThat(userRepository.findByUsername("user")).isEqualTo(user),
                () -> assertThat(roleRepository.findByName("ADMIN")).isNotNull(),
                () -> assertThat(privilegeRepository.findByName("VIEW")).isNotNull()
        );
    }

    @Test
    @Transactional
    @Rollback
    public void ShouldThrow_WhenAddedSameUsername() throws UserExistsException {
        userService.registerNewUser(user);

        User sameUser = new User();
        sameUser.setUsername(user.getUsername());
        sameUser.setPassword("123");
        sameUser.setRolePrivilegeCollection(List.of(adminEditor));

        Assertions.assertThrows(UserExistsException.class, () -> userService.registerNewUser(sameUser));

    }

    @Configuration
    @ComponentScan(basePackages = "ru.shadi777.proxyapplication")
    static class JpaConfig {

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            var result = new JpaTransactionManager();
            result.setDataSource(dataSource);
            return result;
        }

    }

}
