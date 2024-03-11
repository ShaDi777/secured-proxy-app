package ru.shadi777.proxyapplication.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.shadi777.proxyapplication.models.User;
import ru.shadi777.proxyapplication.repositories.PrivilegeRepository;
import ru.shadi777.proxyapplication.repositories.RolePrivilegeRepository;
import ru.shadi777.proxyapplication.repositories.RoleRepository;
import ru.shadi777.proxyapplication.repositories.UserRepository;
import ru.shadi777.proxyapplication.utils.exceptions.UserExistsException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public User registerNewUser(User user) throws UserExistsException {
        User registeredUser = userRepository.findByUsername(user.getUsername());
        if (registeredUser != null) {
            throw new UserExistsException("Username " + user.getUsername() + " is already taken!");
        }

        user.setRolePrivilegeCollection(
                user.getRolePrivilegeCollection()
                    .stream()
                    .map(rolePrivilege ->
                        rolePrivilegeRepository.findByRoleAndPrivilege(
                                roleRepository.findByName(rolePrivilege.getRole().getName()),
                                privilegeRepository.findByName(rolePrivilege.getPrivilege().getName())
                        )
                    ).toList()
        );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
