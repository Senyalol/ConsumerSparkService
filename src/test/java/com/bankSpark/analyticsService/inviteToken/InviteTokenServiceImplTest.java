package com.bankSpark.analyticsService.inviteToken;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.TokenResponseDTO;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;
import com.bankSpark.analyticsService.security.randomKeyAPI.InviteTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InviteTokenServiceImpl Unit Tests")
class InviteTokenServiceImplTest {

    @Mock
    private InviteTokenRepository inviteTokenRepository;

    @InjectMocks
    private InviteTokenServiceImpl inviteTokenService;

    private InviteToken testToken;
    private InviteToken testUsedToken;

    @BeforeEach
    void setUp() {
        testToken = new InviteToken();
        testToken.setId(1);
        testToken.setToken("ANALYST-ABC123");
        testToken.setRole("ANALYST");
        testToken.setUsed(false);
        testToken.setExpiresAt(Instant.now().plusSeconds(86400));
        testToken.setCreatedAt(Instant.now());

        testUsedToken = new InviteToken();
        testUsedToken.setId(2);
        testUsedToken.setToken("ANALYST-DEF456");
        testUsedToken.setRole("ANALYST");
        testUsedToken.setUsed(true);
        testUsedToken.setExpiresAt(Instant.now().plusSeconds(86400));
        testUsedToken.setCreatedAt(Instant.now());
    }

    @Nested
    @DisplayName("generateToken() tests")
    class GenerateTokenTests {

        @Test
        @DisplayName("Should generate token successfully with default values")
        void shouldGenerateTokenWithDefaultValues() {
            // given
            when(inviteTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());
            when(inviteTokenRepository.save(any(InviteToken.class))).thenReturn(testToken);

            // when
            TokenResponseDTO result = inviteTokenService.generateToken(null, null);

            // then
            assertNotNull(result);
            assertNotNull(result.getToken());
            assertNotNull(result.getRole());
            assertNotNull(result.getExpiresAt());
            assertNotNull(result.getCreatedAt());
            verify(inviteTokenRepository, times(1)).save(any(InviteToken.class));
        }

        @Test
        @DisplayName("Should generate token with custom role and hours")
        void shouldGenerateTokenWithCustomValues() {
            // given
            when(inviteTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());
            when(inviteTokenRepository.save(any(InviteToken.class))).thenReturn(testToken);

            // when
            TokenResponseDTO result = inviteTokenService.generateToken("ADMIN", 48);

            // then
            assertNotNull(result);
            verify(inviteTokenRepository, times(1)).save(any(InviteToken.class));
        }
    }

    @Nested
    @DisplayName("generateDefaultToken() tests")
    class GenerateDefaultTokenTests {

        @Test
        @DisplayName("Should generate default token")
        void shouldGenerateDefaultToken() {
            // given
            when(inviteTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());
            when(inviteTokenRepository.save(any(InviteToken.class))).thenReturn(testToken);

            // when
            TokenResponseDTO result = inviteTokenService.generateDefaultToken();

            // then
            assertNotNull(result);
            verify(inviteTokenRepository, times(1)).save(any(InviteToken.class));
        }
    }

    @Nested
    @DisplayName("isValidToken() tests")
    class IsValidTokenTests {

        @Test
        @DisplayName("Should return true for valid token")
        void shouldReturnTrueForValidToken() {
            // given
            when(inviteTokenRepository.findByTokenAndUsedFalseAndExpiresAtAfter(anyString(), any(LocalDateTime.class)))
                    .thenReturn(Optional.of(testToken));

            // when
            boolean result = inviteTokenService.isValidToken("ANALYST-ABC123");

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for invalid token")
        void shouldReturnFalseForInvalidToken() {
            // given
            when(inviteTokenRepository.findByTokenAndUsedFalseAndExpiresAtAfter(anyString(), any(LocalDateTime.class)))
                    .thenReturn(Optional.empty());

            // when
            boolean result = inviteTokenService.isValidToken("INVALID-TOKEN");

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("getTokenInfo() tests")
    class GetTokenInfoTests {

        @Test
        @DisplayName("Should return token info when token exists")
        void shouldReturnTokenInfoWhenTokenExists() {
            // given
            when(inviteTokenRepository.findByToken("ANALYST-ABC123")).thenReturn(Optional.of(testToken));

            // when
            InviteToken result = inviteTokenService.getTokenInfo("ANALYST-ABC123");

            // then
            assertNotNull(result);
            assertEquals("ANALYST-ABC123", result.getToken());
            verify(inviteTokenRepository, times(1)).findByToken("ANALYST-ABC123");
        }

        @Test
        @DisplayName("Should throw exception when token not found")
        void shouldThrowExceptionWhenTokenNotFound() {
            // given
            when(inviteTokenRepository.findByToken("UNKNOWN")).thenReturn(Optional.empty());

            // when & then
            assertThrows(RuntimeException.class, () -> inviteTokenService.getTokenInfo("UNKNOWN"));
        }
    }

    @Nested
    @DisplayName("revokeToken() tests")
    class RevokeTokenTests {

        @Test
        @DisplayName("Should revoke unused token successfully")
        void shouldRevokeUnusedTokenSuccessfully() {
            // given
            when(inviteTokenRepository.findByToken("ANALYST-ABC123")).thenReturn(Optional.of(testToken));

            // when
            inviteTokenService.revokeToken("ANALYST-ABC123");

            // then
            verify(inviteTokenRepository, times(1)).delete(testToken);
        }

        @Test
        @DisplayName("Should throw exception when revoking used token")
        void shouldThrowExceptionWhenRevokingUsedToken() {
            // given
            when(inviteTokenRepository.findByToken("ANALYST-DEF456")).thenReturn(Optional.of(testUsedToken));

            // when & then
            assertThrows(RuntimeException.class, () -> inviteTokenService.revokeToken("ANALYST-DEF456"));
            verify(inviteTokenRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Should throw exception when token not found")
        void shouldThrowExceptionWhenTokenNotFound() {
            // given
            when(inviteTokenRepository.findByToken("UNKNOWN")).thenReturn(Optional.empty());

            // when & then
            assertThrows(RuntimeException.class, () -> inviteTokenService.revokeToken("UNKNOWN"));
        }
    }

    @Nested
    @DisplayName("cleanupExpiredTokens() tests")
    class CleanupExpiredTokensTests {

        @Test
        @DisplayName("Should clean up expired tokens")
        void shouldCleanUpExpiredTokens() {
            // given
            when(inviteTokenRepository.countByUsedFalseAndExpiresAtBefore(any(Instant.class))).thenReturn(5);
            when(inviteTokenRepository.deleteByUsedFalseAndExpiresAtBefore(any(Instant.class))).thenReturn(5);

            // when
            int result = inviteTokenService.cleanupExpiredTokens();

            // then
            assertEquals(5, result);
            verify(inviteTokenRepository, times(1)).countByUsedFalseAndExpiresAtBefore(any(Instant.class));
            verify(inviteTokenRepository, times(1)).deleteByUsedFalseAndExpiresAtBefore(any(Instant.class));
        }

        @Test
        @DisplayName("Should return zero when no expired tokens")
        void shouldReturnZeroWhenNoExpiredTokens() {
            // given
            when(inviteTokenRepository.countByUsedFalseAndExpiresAtBefore(any(Instant.class))).thenReturn(0);
            when(inviteTokenRepository.deleteByUsedFalseAndExpiresAtBefore(any(Instant.class))).thenReturn(0);

            // when
            int result = inviteTokenService.cleanupExpiredTokens();

            // then
            assertEquals(0, result);
        }
    }

    @Nested
    @DisplayName("getAllInviteTokens() tests")
    class GetAllInviteTokensTests {

        @Test
        @DisplayName("Should return all invite tokens")
        void shouldReturnAllInviteTokens() {
            // given
            List<InviteToken> tokens = Arrays.asList(testToken, testUsedToken);
            when(inviteTokenRepository.findAll()).thenReturn(tokens);

            // when
            List<FullTokenInfoDTO> result = inviteTokenService.getAllInviteTokens();

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(inviteTokenRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no tokens exist")
        void shouldReturnEmptyListWhenNoTokens() {
            // given
            when(inviteTokenRepository.findAll()).thenReturn(List.of());

            // when
            List<FullTokenInfoDTO> result = inviteTokenService.getAllInviteTokens();

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getAllInviteTokensByUsed() tests")
    class GetAllInviteTokensByUsedTests {

        @Test
        @DisplayName("Should return unused tokens when used = false")
        void shouldReturnUnusedTokens() {
            // given
            List<InviteToken> tokens = Arrays.asList(testToken, testUsedToken);
            when(inviteTokenRepository.findAll()).thenReturn(tokens);

            // when
            List<FullTokenInfoDTO> result = inviteTokenService.getAllInviteTokensByUsed(false);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertFalse(result.get(0).getUsed());
        }

        @Test
        @DisplayName("Should return used tokens when used = true")
        void shouldReturnUsedTokens() {
            // given
            List<InviteToken> tokens = Arrays.asList(testToken, testUsedToken);
            when(inviteTokenRepository.findAll()).thenReturn(tokens);

            // when
            List<FullTokenInfoDTO> result = inviteTokenService.getAllInviteTokensByUsed(true);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).getUsed());
        }

        @Test
        @DisplayName("Should throw exception when used is null")
        void shouldThrowExceptionWhenUsedIsNull() {
            // when & then
            assertThrows(RuntimeException.class, () -> inviteTokenService.getAllInviteTokensByUsed(null));
        }
    }
}