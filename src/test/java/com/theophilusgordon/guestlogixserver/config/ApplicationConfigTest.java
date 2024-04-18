package com.theophilusgordon.guestlogixserver.config;

import com.theophilusgordon.guestlogixserver.auditing.ApplicationAuditAware;
import com.theophilusgordon.guestlogixserver.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ApplicationConfigTest {

    @Mock
    private UserRepository repository;

    @Mock
    private AuthenticationConfiguration config;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenUserDetailsService_thenNotNull() {
        UserDetailsService result = applicationConfig.userDetailsService();

        assertTrue(result != null);
    }

    @Test
    void whenAuthenticationProvider_thenNotNull() {
        AuthenticationProvider result = applicationConfig.authenticationProvider();

        assertTrue(result instanceof DaoAuthenticationProvider);
    }

    @Test
    void whenAuditorAware_thenNotNull() {
        AuditorAware<String> result = applicationConfig.auditorAware();

        assertTrue(result instanceof ApplicationAuditAware);
    }

    @Test
    void whenAuthenticationManager_thenNotNull() throws Exception {
        when(config.getAuthenticationManager()).thenReturn(null);

        var result = applicationConfig.authenticationManager(config);

        assertTrue(result == null);
    }

    @Test
    void whenPasswordEncoder_thenNotNull() {
        PasswordEncoder result = applicationConfig.passwordEncoder();

        assertTrue(result instanceof BCryptPasswordEncoder);
    }
}