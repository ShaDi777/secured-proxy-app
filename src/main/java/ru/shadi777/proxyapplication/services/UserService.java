package ru.shadi777.proxyapplication.services;

import ru.shadi777.proxyapplication.models.User;
import ru.shadi777.proxyapplication.utils.exceptions.UserExistsException;

public interface UserService {
    User findByUsername(String username);
    User registerNewUser(User user) throws UserExistsException;
}
