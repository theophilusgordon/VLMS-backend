package com.theophilusgordon.vlmsbackend.token;

import com.theophilusgordon.vlmsbackend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.argThat;

class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUserToken() {
        User user = new User();
        user.setId(UUID.randomUUID());
        String jwtToken = "sampleJwtToken";

        tokenService.saveUserToken(user, jwtToken);

        verify(tokenRepository).save(argThat(token ->
                token.getUser().equals(user) &&
                        token.getToken().equals(jwtToken) &&
                        token.getTokenType() == TokenType.BEARER &&
                        !token.isExpired() &&
                        !token.isRevoked()
        ));
    }
}
