package ru.shadi777.proxyapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shadi777.proxyapplication.models.Privilege;
import ru.shadi777.proxyapplication.models.Role;
import ru.shadi777.proxyapplication.models.RolePrivilege;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
    RolePrivilege findByRoleAndPrivilege(Role role, Privilege privilege);
}
