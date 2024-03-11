package ru.shadi777.proxyapplication.dto;

import lombok.Data;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.enums.RoleType;
import ru.shadi777.proxyapplication.models.Privilege;
import ru.shadi777.proxyapplication.models.Role;
import ru.shadi777.proxyapplication.models.RolePrivilege;
import ru.shadi777.proxyapplication.models.User;

import java.util.Collection;

@Data
public class UserDto {
    String username;
    String password;
    Collection<RolePrivilegeDto> rolePrivilegeCollection;

    public User mapToUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);
        user.setLocked(false);
        user.setRolePrivilegeCollection(
                rolePrivilegeCollection.stream()
                        .map(rolePrivilegeDto ->
                                new RolePrivilege(
                                        new Role(rolePrivilegeDto.getRoleType().name()),
                                        new Privilege(rolePrivilegeDto.getPrivilegeType().name())
                                )
                        ).toList()
        );
        return user;
    }
}
