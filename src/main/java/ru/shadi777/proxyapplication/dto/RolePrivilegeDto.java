package ru.shadi777.proxyapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.enums.RoleType;

@Data
@AllArgsConstructor
public class RolePrivilegeDto {
    RoleType roleType;
    PrivilegeType privilegeType;
}
