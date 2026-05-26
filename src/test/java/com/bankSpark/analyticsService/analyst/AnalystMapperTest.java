package com.bankSpark.analyticsService.analyst;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import com.bankSpark.analyticsService.mapper.AnalystMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnalystMapper Unit Tests")
class AnalystMapperTest {

    private AnalystMapper analystMapper;
    private Analyst testAnalyst;
    private InviteToken testToken;

    @BeforeEach
    void setUp() {
        analystMapper = new AnalystMapper();

        testToken = new InviteToken();
        testToken.setId(1);
        testToken.setToken("ANALYST-ABC123");
        testToken.setRole("ANALYST");

        testAnalyst = new Analyst();
        testAnalyst.setId(1);
        testAnalyst.setToken(testToken);
        testAnalyst.setLogin("john_analyst");
        testAnalyst.setPassword("encoded_password_hash");
        testAnalyst.setRole("ANALYST");
        testAnalyst.setCreatedAt(Instant.now());
    }

    @Nested
    @DisplayName("toFullInfoDTO() tests")
    class ToFullInfoDTOTests {

        @Test
        @DisplayName("Should convert Analyst entity to AnalystInfoDTO successfully")
        void shouldConvertAnalystToFullInfoDTO() {
            // when
            AnalystInfoDTO result = analystMapper.toFullInfoDTO(testAnalyst);

            // then
            assertNotNull(result);
            assertEquals("ANALYST-ABC123", result.getToken());
            assertEquals("john_analyst", result.getLogin());
            assertEquals("encoded_password_hash", result.getPassword());
            assertEquals("ANALYST", result.getRole());
            assertNotNull(result.getCreatedAt());
        }

        @Test
        @DisplayName("Should handle null analyst gracefully")
        void shouldHandleNullAnalyst() {
            // when & then
            assertThrows(NullPointerException.class, () -> analystMapper.toFullInfoDTO(null));
        }
    }
}