package ru.shadi777.proxyapplication.controllers.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.shadi777.proxyapplication.dto.RolePrivilegeDto;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.enums.RoleType;

public record RolePrivilegeRequest(
        @JsonProperty(value = "role") RoleType role,
        @JsonProperty(value = "privilege") PrivilegeType privilege
) {
    public RolePrivilegeDto mapToRolePrivilegeDto() {
        return new RolePrivilegeDto(role, privilege);
    }
}
