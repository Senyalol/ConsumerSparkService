package com.bankSpark.analyticsService.inviteToken;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import com.bankSpark.analyticsService.mapper.InviteTokenMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InviteTokenMapper Unit Tests")
class InviteTokenMapperTest {

    private InviteToken testToken;

    @BeforeEach
    void setUp() {
        testToken = new InviteToken();
        testToken.setId(1);
        testToken.setToken("ANALYST-ABC123");
        testToken.setRole("ANALYST");
        testToken.setUsed(false);
        testToken.setExpiresAt(Instant.now().plusSeconds(86400));
        testToken.setCreatedAt(Instant.now());
    }

    @Nested
    @DisplayName("toFullDTO() tests")
    class ToFullDTOTests {

        @Test
        @DisplayName("Should convert InviteToken to FullTokenInfoDTO successfully")
        void shouldConvertInviteTokenToFullDTO() {
            // when
            FullTokenInfoDTO result = InviteTokenMapper.toFullDTO(testToken);

            // then
            assertNotNull(result);
            assertEquals("ANALYST-ABC123", result.getToken());
            assertEquals("ANALYST", result.getRole());
            assertFalse(result.getUsed());
            assertNotNull(result.getExpiresAt());
            assertNotNull(result.getCreatedAt());
        }

        @Test
        @DisplayName("Should handle null token gracefully")
        void shouldHandleNullToken() {
            // when & then
            assertThrows(NullPointerException.class, () -> InviteTokenMapper.toFullDTO(null));
        }
    }
}