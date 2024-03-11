package ru.shadi777.proxyapplication.controllers.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.shadi777.proxyapplication.dto.UserDto;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.enums.RoleType;

import java.util.Arrays;

public record RegisterUserRequest(
        @JsonProperty(value = "username") String username,
        @JsonProperty(value = "password") String password,
        @JsonProperty(value = "role_privilege") RolePrivilegeRequest[] rolePrivilegeRequests
) {
    public UserDto mapToUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setRolePrivilegeCollection(
                Arrays.stream(rolePrivilegeRequests)
                        .map(RolePrivilegeRequest::mapToRolePrivilegeDto)
                        .toList()
        );

        return userDto;
    }
}
