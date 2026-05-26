package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import com.bankSpark.analyticsService.repository.AnalystRepository;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWTServiceImpl Unit Tests")
class JWTServiceImplTest {

    @Mock
    private AnalystRepository analystRepository;

    @InjectMocks
    private JWTServiceImpl jwtService;

    private Analyst testAnalyst;
    private InviteToken testToken;
    private SecretKey secretKey;

    // ✅ ПРАВИЛЬНЫЙ КЛЮЧ: 256 бит (32 байта) в Base64
    private final String testSignatureKey = "dGhpc2lzMzJieXRlc2tleWZvcnNpZ25hdHVyZWFuZHNlY3VyZQ==";

    @BeforeEach
    void setUp() throws Exception {
        testToken = new InviteToken();
        testToken.setId(1);
        testToken.setToken("ANALYST-ABC123");
        testToken.setRole("ANALYST");

        testAnalyst = new Analyst();
        testAnalyst.setId(1);
        testAnalyst.setToken(testToken);
        testAnalyst.setLogin("john_analyst");
        testAnalyst.setPassword("encoded_password");
        testAnalyst.setRole("ANALYST");
        testAnalyst.setCreatedAt(Instant.now());

        ReflectionTestUtils.setField(jwtService, "signatureKey", testSignatureKey);

        // Получаем секретный ключ через рефлексию
        java.lang.reflect.Method method = JWTServiceImpl.class.getDeclaredMethod("getSignInKey");
        method.setAccessible(true);
        secretKey = (SecretKey) method.invoke(jwtService);
    }

    @Nested
    @DisplayName("getRoleAnalyst() tests")
    class GetRoleAnalystTests {

        @Test
        @DisplayName("Should return role when analyst exists")
        void shouldReturnRoleWhenAnalystExists() {
            when(analystRepository.getAnalystByLogin("john_analyst")).thenReturn(Optional.of(testAnalyst));
            String result = jwtService.getRoleAnalyst("john_analyst");
            assertEquals("ANALYST", result);
            verify(analystRepository, times(1)).getAnalystByLogin("john_analyst");
        }

        @Test
        @DisplayName("Should throw exception when analyst not found")
        void shouldThrowExceptionWhenAnalystNotFound() {
            when(analystRepository.getAnalystByLogin("unknown")).thenReturn(Optional.empty());
            assertThrows(Exception.class, () -> jwtService.getRoleAnalyst("unknown"));
        }
    }

    @Nested
    @DisplayName("getTokenAnalyst() tests")
    class GetTokenAnalystTests {

        @Test
        @DisplayName("Should return token when analyst exists")
        void shouldReturnTokenWhenAnalystExists() {
            when(analystRepository.getAnalystByLogin("john_analyst")).thenReturn(Optional.of(testAnalyst));
            String result = jwtService.getTokenAnalyst("john_analyst");
            assertEquals("ANALYST-ABC123", result);
            verify(analystRepository, times(1)).getAnalystByLogin("john_analyst");
        }
    }

    @Nested
    @DisplayName("validateJwtToken() tests")
    class ValidateJwtTokenTests {

        @Test
        @DisplayName("Should return true for valid token")
        void shouldReturnTrueForValidToken() {
            String validToken = generateValidJwtToken();
            assertTrue(jwtService.validateJwtToken(validToken));
        }

        @Test
        @DisplayName("Should return false for expired token")
        void shouldReturnFalseForExpiredToken() {
            String expiredToken = generateExpiredJwtToken();
            assertFalse(jwtService.validateJwtToken(expiredToken));
        }

        @Test
        @DisplayName("Should return false for malformed token")
        void shouldReturnFalseForMalformedToken() {
            assertFalse(jwtService.validateJwtToken("malformed.token.here"));
        }

        @Test
        @DisplayName("Should return false for null token")
        void shouldReturnFalseForNullToken() {
            assertFalse(jwtService.validateJwtToken(null));
        }
    }

    @Nested
    @DisplayName("generateJwtToken() tests")
    class GenerateJwtTokenTests {

        @Test
        @DisplayName("Should generate valid JWT token")
        void shouldGenerateValidJwtToken() {
            String result = jwtService.generateJwtToken("john_analyst", "ANALYST", "password", "TOKEN-123");
            assertNotNull(result);
            assertTrue(result.split("\\.").length == 3);
        }
    }

    @Nested
    @DisplayName("getTokenForAnalyst() tests")
    class GetTokenForAnalystTests {

        @Test
        @DisplayName("Should return JwtAuthenticationDTO for valid analyst")
        void shouldReturnJwtAuthenticationDTOForValidAnalyst() {
            when(analystRepository.getAnalystByLogin("john_analyst")).thenReturn(Optional.of(testAnalyst));

            JwtAuthenticationDTO result = jwtService.getTokenForAnalyst("john_analyst");

            assertNotNull(result);
            assertNotNull(result.getToken());
            assertNotNull(result.getRefreshToken());
            verify(analystRepository, atLeastOnce()).getAnalystByLogin("john_analyst");
        }
    }

    @Nested
    @DisplayName("generateRefreshToken() tests")
    class GenerateRefreshTokenTests {

        @Test
        @DisplayName("Should generate refresh token response")
        void shouldGenerateRefreshTokenResponse() {
            String refreshToken = "old-refresh-token";
            when(analystRepository.getAnalystByLogin("john_analyst")).thenReturn(Optional.of(testAnalyst));

            JwtAuthenticationDTO result = jwtService.generateRefreshToken("john_analyst", refreshToken);

            assertNotNull(result);
            assertNotNull(result.getToken());
            assertEquals(refreshToken, result.getRefreshToken());
        }
    }

    @Nested
    @DisplayName("getLoginFromToken() tests")
    class GetLoginFromTokenTests {

        @Test
        @DisplayName("Should extract login from valid token")
        void shouldExtractLoginFromValidToken() {
            String login = "john_analyst";
            String token = generateJwtTokenWithSubject(login);

            String result = jwtService.getLoginFromToken(token);

            assertEquals(login, result);
        }
    }

    @Nested
    @DisplayName("getOutFromAccount() tests")
    class GetOutFromAccountTests {

        @Test
        @DisplayName("Should return expired JWT token for logout")
        void shouldReturnExpiredJwtTokenForLogout() {
            when(analystRepository.getAnalystByLogin("john_analyst")).thenReturn(Optional.of(testAnalyst));

            JwtAuthenticationDTO result = jwtService.getOutFromAccount("john_analyst");

            assertNotNull(result);
            assertNotNull(result.getToken());
            assertNotNull(result.getRefreshToken());
            assertFalse(jwtService.validateJwtToken(result.getToken()));
        }
    }

    // ========== Вспомогательные методы ==========

    private String generateValidJwtToken() {
        Date expiration = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject("john_analyst")
                .claim("role", "ANALYST")
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    private String generateExpiredJwtToken() {
        Date expiration = Date.from(LocalDateTime.now().minusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject("john_analyst")
                .claim("role", "ANALYST")
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    private String generateJwtTokenWithSubject(String subject) {
        Date expiration = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }
}