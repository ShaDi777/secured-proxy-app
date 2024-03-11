package ru.shadi777.proxyapplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.models.RolePrivilege;
import ru.shadi777.proxyapplication.enums.RoleType;
import ru.shadi777.proxyapplication.repositories.UserRepository;
import ru.shadi777.proxyapplication.services.AuthenticationUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private UserRepository userRepository;

    public UserDetailsService getUserDetailsService() {
        return new AuthenticationUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy =
                """
                        ADMIN_EDIT > POSTS_EDIT > POSTS_VIEW
                        ADMIN_EDIT > USERS_EDIT > USERS_VIEW
                        ADMIN_EDIT > ALBUMS_EDIT > ALBUMS_VIEW
                        ADMIN_VIEW > POSTS_VIEW
                        ADMIN_VIEW > USERS_VIEW
                        ADMIN_VIEW > ALBUMS_VIEW
                        """;
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(getUserDetailsService()).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authCustomizer ->
                        authCustomizer
                                // POSTS
                                .requestMatchers(HttpMethod.GET, "/api/posts/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.POSTS, PrivilegeType.VIEW))

                                .requestMatchers(HttpMethod.POST, "/api/posts/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.POSTS, PrivilegeType.EDIT))

                                .requestMatchers(HttpMethod.PUT, "/api/posts/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.POSTS, PrivilegeType.EDIT))

                                .requestMatchers(HttpMethod.DELETE, "/api/posts/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.POSTS, PrivilegeType.EDIT))

                                //USERS
                                .requestMatchers(HttpMethod.GET, "/api/users/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.USERS, PrivilegeType.VIEW))

                                .requestMatchers(HttpMethod.POST, "/api/users/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.USERS, PrivilegeType.EDIT))

                                .requestMatchers(HttpMethod.PUT, "/api/users/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.USERS, PrivilegeType.EDIT))

                                .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.USERS, PrivilegeType.EDIT))

                                // ALBUMS
                                .requestMatchers(HttpMethod.GET, "/api/albums/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.ALBUMS, PrivilegeType.VIEW))

                                .requestMatchers(HttpMethod.POST, "/api/albums/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.ALBUMS, PrivilegeType.EDIT))

                                .requestMatchers(HttpMethod.PUT, "/api/albums/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.ALBUMS, PrivilegeType.EDIT))

                                .requestMatchers(HttpMethod.DELETE, "/api/albums/**")
                                .hasAuthority(RolePrivilege.Authority(RoleType.ALBUMS, PrivilegeType.EDIT))

                                // REGISTRATION
                                .requestMatchers(HttpMethod.POST, "/register")
                                .hasAuthority(RolePrivilege.Authority(RoleType.ADMIN, PrivilegeType.EDIT))
                )
                .httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager(http));

        return http.build();
    }
}
