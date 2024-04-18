package com.theophilusgordon.guestlogixserver.auditing;

import com.theophilusgordon.guestlogixserver.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ApplicationAuditAwareTest {

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ApplicationAuditAware applicationAuditAware;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void whenAuthenticationIsNotAuthenticated_thenCurrentAuditorIsEmpty() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        Optional<String> result = applicationAuditAware.getCurrentAuditor();

        assertEquals(Optional.empty(), result);
    }

    @Test
    void whenAuthenticationIsAuthenticatedAndNotAnonymous_thenCurrentAuditorIsUserId() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        when(authentication.getPrincipal()).thenReturn(user);

        Optional<String> result = applicationAuditAware.getCurrentAuditor();

        assertEquals(Optional.of(user.getId()), result);
    }
}