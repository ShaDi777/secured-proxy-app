package ru.shadi777.proxyapplication;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.shadi777.proxyapplication.enums.PrivilegeType;
import ru.shadi777.proxyapplication.enums.RoleType;
import ru.shadi777.proxyapplication.models.RolePrivilege;
import ru.shadi777.proxyapplication.services.audit.AuditInterceptor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProxyControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuditInterceptor interceptor;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void ShouldResponseUnauthorized_WhenNoAuthorization() throws Exception {
        mockMvc.perform(get("/api/posts")).andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/users/1")).andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/albums")).andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/posts/1")).andExpect(status().isUnauthorized());
    }

    @Test
    public void ShouldResponseOk_WhenAuthorizedAndHasAccess() throws Exception {
        var postsViewerAuthority = new SimpleGrantedAuthority(RolePrivilege.Authority(RoleType.POSTS, PrivilegeType.VIEW));
        var albumsEditorAuthority = new SimpleGrantedAuthority(RolePrivilege.Authority(RoleType.ALBUMS, PrivilegeType.EDIT));
        var adminEditorAuthority = new SimpleGrantedAuthority(RolePrivilege.Authority(RoleType.ADMIN, PrivilegeType.EDIT));

        mockMvc.perform(get("/api/posts").with(user("postsViewer").authorities(postsViewerAuthority)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/albums/1").with(user("albumsEditor").authorities(albumsEditorAuthority)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/users").with(user("adminEditor").authorities(adminEditorAuthority)))
                .andExpect(status().isOk());
    }

    @Test
    public void ShouldResponseForbidden_WhenNoAccess() throws Exception {
        var postsViewerAuthority = new SimpleGrantedAuthority(RolePrivilege.Authority(RoleType.POSTS, PrivilegeType.VIEW));
        var usersViewerAuthority = new SimpleGrantedAuthority(RolePrivilege.Authority(RoleType.USERS, PrivilegeType.VIEW));
        var albumsViewerAuthority = new SimpleGrantedAuthority(RolePrivilege.Authority(RoleType.ALBUMS, PrivilegeType.VIEW));

        mockMvc.perform(get("/api/posts").with(user("usersViewer").authorities(usersViewerAuthority)))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/posts/1").with(user("albumsViewer").authorities(albumsViewerAuthority)))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/users/1").with(user("usersViewer").authorities(usersViewerAuthority)))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/users").with(user("usersViewer").authorities(usersViewerAuthority)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/posts/1").with(user("postsViewer").authorities(postsViewerAuthority)))
                .andExpect(status().isForbidden());
    }

}