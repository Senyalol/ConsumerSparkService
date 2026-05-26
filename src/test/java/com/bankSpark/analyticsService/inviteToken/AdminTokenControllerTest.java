package com.bankSpark.analyticsService.inviteToken;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.GenerateTokenRequestDTO;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.TokenResponseDTO;
import com.bankSpark.analyticsService.controller.AdminTokenController;
import com.bankSpark.analyticsService.facade.inviteToken.InviteTokenFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminTokenController Unit Tests")
class AdminTokenControllerTest {

    @Mock
    private InviteTokenFacade inviteTokenFacade;

    @InjectMocks
    private AdminTokenController adminTokenController;

    private FullTokenInfoDTO testFullTokenDTO1;
    private FullTokenInfoDTO testFullTokenDTO2;
    private TokenResponseDTO testTokenResponseDTO;
    private GenerateTokenRequestDTO generateRequest;

    @BeforeEach
    void setUp() {
        testFullTokenDTO1 = new FullTokenInfoDTO();
        testFullTokenDTO1.setToken("ANALYST-ABC123");
        testFullTokenDTO1.setRole("ANALYST");
        testFullTokenDTO1.setUsed(false);
        testFullTokenDTO1.setExpiresAt(LocalDateTime.now().plusDays(3));
        testFullTokenDTO1.setCreatedAt(LocalDateTime.now());

        testFullTokenDTO2 = new FullTokenInfoDTO();
        testFullTokenDTO2.setToken("ANALYST-DEF456");
        testFullTokenDTO2.setRole("ANALYST");
        testFullTokenDTO2.setUsed(true);
        testFullTokenDTO2.setExpiresAt(LocalDateTime.now().plusDays(1));
        testFullTokenDTO2.setCreatedAt(LocalDateTime.now().minusDays(2));

        testTokenResponseDTO = new TokenResponseDTO();
        testTokenResponseDTO.setToken("ANALYST-NEW123");
        testTokenResponseDTO.setRole("ANALYST");
        testTokenResponseDTO.setExpiresAt(LocalDateTime.now().plusDays(3));
        testTokenResponseDTO.setCreatedAt(LocalDateTime.now());

        generateRequest = new GenerateTokenRequestDTO();
        generateRequest.setRole("ADMIN");
        generateRequest.setHoursValid(48);
    }

    @Nested
    @DisplayName("getAllTokens() tests")
    class GetAllTokensTests {

        @Test
        @DisplayName("Should return 200 OK with list of tokens")
        void shouldReturn200OkWithTokensList() {
            // given
            List<FullTokenInfoDTO> expectedTokens = Arrays.asList(testFullTokenDTO1, testFullTokenDTO2);
            when(inviteTokenFacade.getAllInviteTokens()).thenReturn(expectedTokens);

            // when
            ResponseEntity<List<FullTokenInfoDTO>> response = adminTokenController.getAllTokens();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(inviteTokenFacade, times(1)).getAllInviteTokens();
        }

        @Test
        @DisplayName("Should return 204 No Content when no tokens exist")
        void shouldReturn204NoContentWhenNoTokens() {
            // given
            when(inviteTokenFacade.getAllInviteTokens()).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<FullTokenInfoDTO>> response = adminTokenController.getAllTokens();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(inviteTokenFacade, times(1)).getAllInviteTokens();
        }
    }

    @Nested
    @DisplayName("getUsedTokens() tests")
    class GetUsedTokensTests {

        @Test
        @DisplayName("Should return 200 OK with filtered tokens")
        void shouldReturn200OkWithFilteredTokens() {
            // given
            List<FullTokenInfoDTO> expectedTokens = Collections.singletonList(testFullTokenDTO2);
            when(inviteTokenFacade.getAllInviteTokensByUsed(true)).thenReturn(expectedTokens);

            // when
            ResponseEntity<List<FullTokenInfoDTO>> response = adminTokenController.getUsedTokens(true);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertTrue(response.getBody().get(0).getUsed());
            verify(inviteTokenFacade, times(1)).getAllInviteTokensByUsed(true);
        }
    }

    @Nested
    @DisplayName("generateToken() tests")
    class GenerateTokenTests {

        @Test
        @DisplayName("Should generate token with custom parameters")
        void shouldGenerateTokenWithCustomParams() {
            // given
            when(inviteTokenFacade.generateToken("ADMIN", 48)).thenReturn(testTokenResponseDTO);

            // when
            ResponseEntity<TokenResponseDTO> response = adminTokenController.generateToken(generateRequest);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(inviteTokenFacade, times(1)).generateToken("ADMIN", 48);
        }

        @Test
        @DisplayName("Should generate token with default parameters when request is null")
        void shouldGenerateTokenWithDefaultParams() {
            // given
            when(inviteTokenFacade.generateToken(null, null)).thenReturn(testTokenResponseDTO);

            // when
            ResponseEntity<TokenResponseDTO> response = adminTokenController.generateToken(null);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(inviteTokenFacade, times(1)).generateToken(null, null);
        }
    }

    @Nested
    @DisplayName("generateDefaultToken() tests")
    class GenerateDefaultTokenTests {

        @Test
        @DisplayName("Should generate default token")
        void shouldGenerateDefaultToken() {
            // given
            when(inviteTokenFacade.generateDefaultToken()).thenReturn(testTokenResponseDTO);

            // when
            ResponseEntity<TokenResponseDTO> response = adminTokenController.generateDefaultToken();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(inviteTokenFacade, times(1)).generateDefaultToken();
        }
    }

    @Nested
    @DisplayName("validateToken() tests")
    class ValidateTokenTests {

        @Test
        @DisplayName("Should return true for valid token")
        void shouldReturnTrueForValidToken() {
            // given
            when(inviteTokenFacade.isValidToken("ANALYST-ABC123")).thenReturn(true);

            // when
            ResponseEntity<Boolean> response = adminTokenController.validateToken("ANALYST-ABC123");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody());
            verify(inviteTokenFacade, times(1)).isValidToken("ANALYST-ABC123");
        }

        @Test
        @DisplayName("Should return false for invalid token")
        void shouldReturnFalseForInvalidToken() {
            // given
            when(inviteTokenFacade.isValidToken("INVALID")).thenReturn(false);

            // when
            ResponseEntity<Boolean> response = adminTokenController.validateToken("INVALID");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody());
            verify(inviteTokenFacade, times(1)).isValidToken("INVALID");
        }
    }

    @Nested
    @DisplayName("revokeToken() tests")
    class RevokeTokenTests {

        @Test
        @DisplayName("Should revoke token successfully")
        void shouldRevokeTokenSuccessfully() {
            // given
            doNothing().when(inviteTokenFacade).revokeToken("ANALYST-ABC123");

            // when
            ResponseEntity<String> response = adminTokenController.revokeToken("ANALYST-ABC123");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Token revoked: ANALYST-ABC123", response.getBody());
            verify(inviteTokenFacade, times(1)).revokeToken("ANALYST-ABC123");
        }
    }

    @Nested
    @DisplayName("getTokenInfo() tests")
    class GetTokenInfoTests {

        @Test
        @DisplayName("Should return token info successfully")
        void shouldReturnTokenInfoSuccessfully() {
            // given
            when(inviteTokenFacade.getTokenInfo("ANALYST-ABC123")).thenReturn(testFullTokenDTO1);

            // when
            ResponseEntity<?> response = adminTokenController.getTokenInfo("ANALYST-ABC123");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(inviteTokenFacade, times(1)).getTokenInfo("ANALYST-ABC123");
        }
    }

    @Nested
    @DisplayName("cleanupExpiredTokens() tests")
    class CleanupExpiredTokensTests {

        @Test
        @DisplayName("Should cleanup expired tokens and return count")
        void shouldCleanupExpiredTokensAndReturnCount() {
            // given
            when(inviteTokenFacade.cleanupExpiredTokens()).thenReturn(5);

            // when
            ResponseEntity<Integer> response = adminTokenController.cleanupExpiredTokens();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(5, response.getBody());
            verify(inviteTokenFacade, times(1)).cleanupExpiredTokens();
        }

        @Test
        @DisplayName("Should return zero when no expired tokens")
        void shouldReturnZeroWhenNoExpiredTokens() {
            // given
            when(inviteTokenFacade.cleanupExpiredTokens()).thenReturn(0);

            // when
            ResponseEntity<Integer> response = adminTokenController.cleanupExpiredTokens();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(0, response.getBody());
            verify(inviteTokenFacade, times(1)).cleanupExpiredTokens();
        }
    }
}