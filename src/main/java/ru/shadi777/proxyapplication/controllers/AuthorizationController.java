package ru.shadi777.proxyapplication.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shadi777.proxyapplication.controllers.requests.RegisterUserRequest;
import ru.shadi777.proxyapplication.models.User;
import ru.shadi777.proxyapplication.services.UserService;
import ru.shadi777.proxyapplication.utils.exceptions.UserExistsException;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class AuthorizationController {
    private final UserService userService;

    @PostMapping
    public User registerUser(@RequestBody RegisterUserRequest registerRequest) throws UserExistsException {
        return userService.registerNewUser(registerRequest.mapToUserDto().mapToUser());
    }
}
