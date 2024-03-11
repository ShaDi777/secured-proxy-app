package ru.shadi777.proxyapplication.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.enums.RoleType;
import ru.shadi777.proxyapplication.models.Role;
import ru.shadi777.proxyapplication.models.RolePrivilege;
import ru.shadi777.proxyapplication.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class AuthenticationUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var dbUser = userRepository.findByUsername(username);
        if (dbUser == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        var roles = dbUser.getRolePrivilegeCollection().stream().map(RolePrivilege::getRole).map(Role::getName).toArray(String[]::new);
        var authorities = getAuthorities(dbUser.getRolePrivilegeCollection());

        return User.builder()
                .username(dbUser.getUsername())
                .password(dbUser.getPassword())
                .disabled(!dbUser.isEnabled())
                .accountLocked(dbUser.isLocked())
                .roles(roles)
                .authorities(authorities)
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<RolePrivilege> rolePrivilegeCollection) {
        return getGrantedAuthorities(
                rolePrivilegeCollection.stream().map(rp ->
                        RolePrivilege.Authority(
                                RoleType.valueOf(rp.getRole().getName()),
                                PrivilegeType.valueOf(rp.getPrivilege().getName())
                        )
                )
        );
    }

    private List<GrantedAuthority> getGrantedAuthorities(Stream<String> authorityStringStream) {
        return authorityStringStream.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
