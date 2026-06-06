package com.enterprise.empmgmt.security;

import com.enterprise.empmgmt.domain.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityUtilsTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void currentUserEmail_anonymousWhenNoAuth() {
        SecurityContextHolder.clearContext();
        assertEquals("anonymous", SecurityUtils.currentUserEmail());
    }

    @Test
    void currentUserEmail_withAuth() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("user@example.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals("user@example.com", SecurityUtils.currentUserEmail());
    }

    @Test
    void currentUser_returnsCustomUserDetails() {
        User user = User.builder().email("u@example.com").password("p").enabled(true).build();
        CustomUserDetails details = new CustomUserDetails(user);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(details);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertSame(details, SecurityUtils.currentUser());
    }
}
