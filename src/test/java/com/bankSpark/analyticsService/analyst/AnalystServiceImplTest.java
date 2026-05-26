package com.bankSpark.analyticsService.analyst;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import com.bankSpark.analyticsService.mapper.AnalystMapper;
import com.bankSpark.analyticsService.repository.AnalystRepository;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;
import com.bankSpark.analyticsService.security.JWTService;
import com.bankSpark.analyticsService.security.analystService.AnalystServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnalystServiceImpl Unit Tests")
class AnalystServiceImplTest {

    @Mock
    private AnalystRepository analystRepository;

    @Mock
    private AnalystMapper analystMapper;

    @Mock
    private InviteTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AnalystServiceImpl analystService;

    private Analyst testAnalyst;
    private InviteToken testToken;
    private CreateAnalystDTO createDTO;
    private UpdateAnalystDTO updateDTO;
    private AnalystInfoDTO testAnalystInfoDTO;

    @BeforeEach
    void setUp() {
        testToken = new InviteToken();
        testToken.setId(1);
        testToken.setToken("ANALYST-ABC123");
        testToken.setRole("ANALYST");
        testToken.setUsed(false);

        testAnalyst = new Analyst();
        testAnalyst.setId(1);
        testAnalyst.setToken(testToken);
        testAnalyst.setLogin("john_analyst");
        testAnalyst.setPassword("encoded_password");
        testAnalyst.setRole("ANALYST");
        testAnalyst.setCreatedAt(Instant.now());

        createDTO = new CreateAnalystDTO();
        createDTO.setToken("ANALYST-ABC123");
        createDTO.setLogin("john_analyst");
        createDTO.setPassword("password123");

        updateDTO = new UpdateAnalystDTO();
        updateDTO.setLogin("john_updated");

        testAnalystInfoDTO = new AnalystInfoDTO();
        testAnalystInfoDTO.setLogin("john_analyst");
        testAnalystInfoDTO.setRole("ANALYST");
        testAnalystInfoDTO.setToken("ANALYST-ABC123");
    }

    @Nested
    @DisplayName("createAnalyst() tests")
    class CreateAnalystTests {

        @Test
        @DisplayName("Should create analyst successfully")
        void shouldCreateAnalystSuccessfully() {
            // given
            when(tokenRepository.findByToken("ANALYST-ABC123")).thenReturn(Optional.of(testToken));

            when(analystRepository.getAnalystByLogin("john_analyst"))
                    .thenReturn(Optional.empty())      // 1-й вызов: проверка существования
                    .thenReturn(Optional.of(testAnalyst)); // 2-й вызов: получение после сохранения

            when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

            when(analystRepository.save(any(Analyst.class))).thenAnswer(invocation -> {
                Analyst saved = invocation.getArgument(0);
                saved.setId(1);
                return saved;
            });

            when(analystMapper.toFullInfoDTO(testAnalyst)).thenReturn(testAnalystInfoDTO);

            // when
            AnalystInfoDTO result = analystService.createAnalyst(createDTO);

            // then
            assertNotNull(result);
            assertEquals("john_analyst", result.getLogin());
            assertEquals("ANALYST", result.getRole());
            assertEquals("ANALYST-ABC123", result.getToken());

            verify(analystRepository, times(1)).save(any(Analyst.class));
            verify(tokenRepository, times(1)).save(testToken);
            assertTrue(testToken.getUsed());
        }
    }

    @Nested
    @DisplayName("getAnalysts() tests")
    class GetAnalystsTests {

        @Test
        @DisplayName("Should return list of all analysts")
        void shouldReturnListOfAllAnalysts() {
            // given
            List<Analyst> analysts = Arrays.asList(testAnalyst);
            when(analystRepository.findAll()).thenReturn(analysts);
            when(analystMapper.toFullInfoDTO(testAnalyst)).thenReturn(testAnalystInfoDTO);

            // when
            List<AnalystInfoDTO> result = analystService.getAnalysts();

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(analystRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no analysts exist")
        void shouldReturnEmptyListWhenNoAnalysts() {
            // given
            when(analystRepository.findAll()).thenReturn(List.of());

            // when
            List<AnalystInfoDTO> result = analystService.getAnalysts();

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getAnalystsByRole() tests")
    class GetAnalystsByRoleTests {

        @Test
        @DisplayName("Should return analysts by role")
        void shouldReturnAnalystsByRole() {
            // given
            List<Analyst> analysts = Arrays.asList(testAnalyst);
            when(analystRepository.findAll()).thenReturn(analysts);
            when(analystMapper.toFullInfoDTO(testAnalyst)).thenReturn(testAnalystInfoDTO);

            // when
            List<AnalystInfoDTO> result = analystService.getAnalystsByRole("ANALYST");

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should return empty list for non-existent role")
        void shouldReturnEmptyListForNonExistentRole() {
            // given
            List<Analyst> analysts = Arrays.asList(testAnalyst);
            when(analystRepository.findAll()).thenReturn(analysts);

            // when
            List<AnalystInfoDTO> result = analystService.getAnalystsByRole("ADMIN");

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getAnalystsAfterCreatedAt() tests")
    class GetAnalystsAfterCreatedAtTests {

        @Test
        @DisplayName("Should return analysts created after date")
        void shouldReturnAnalystsAfterCreatedAt() {
            // given
            List<Analyst> analysts = Arrays.asList(testAnalyst);
            when(analystRepository.findAll()).thenReturn(analysts);
            when(analystMapper.toFullInfoDTO(testAnalyst)).thenReturn(testAnalystInfoDTO);
            Instant afterDate = Instant.now().minusSeconds(3600);

            // when
            List<AnalystInfoDTO> result = analystService.getAnalystsAfterCreatedAt(afterDate);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("getAnalystsBeforeCreatedAt() tests")
    class GetAnalystsBeforeCreatedAtTests {

        @Test
        @DisplayName("Should return analysts created before date")
        void shouldReturnAnalystsBeforeCreatedAt() {
            // given
            List<Analyst> analysts = Arrays.asList(testAnalyst);
            when(analystRepository.findAll()).thenReturn(analysts);
            when(analystMapper.toFullInfoDTO(testAnalyst)).thenReturn(testAnalystInfoDTO);
            Instant beforeDate = Instant.now().plusSeconds(3600);

            // when
            List<AnalystInfoDTO> result = analystService.getAnalystsBeforeCreatedAt(beforeDate);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("getAnalystById() tests")
    class GetAnalystByIdTests {

        @Test
        @DisplayName("Should return analyst by id")
        void shouldReturnAnalystById() {
            // given
            when(analystRepository.findById(1)).thenReturn(Optional.of(testAnalyst));
            when(analystMapper.toFullInfoDTO(testAnalyst)).thenReturn(testAnalystInfoDTO);

            // when
            AnalystInfoDTO result = analystService.getAnalystById(1);

            // then
            assertNotNull(result);
            verify(analystRepository, times(1)).findById(1);
        }

        @Test
        @DisplayName("Should throw exception when analyst not found by id")
        void shouldThrowExceptionWhenAnalystNotFoundById() {
            // given
            when(analystRepository.findById(999)).thenReturn(Optional.empty());

            // when & then
            assertThrows(Exception.class, () -> analystService.getAnalystById(999));
        }
    }

    @Nested
    @DisplayName("getAnalystByLogin() tests")
    class GetAnalystByLoginTests {

        @Test
        @DisplayName("Should return analyst by login")
        void shouldReturnAnalystByLogin() {
            // given
            when(analystRepository.getAnalystByLogin("john_analyst")).thenReturn(Optional.of(testAnalyst));
            when(analystMapper.toFullInfoDTO(testAnalyst)).thenReturn(testAnalystInfoDTO);

            // when
            AnalystInfoDTO result = analystService.getAnalystByLogin("john_analyst");

            // then
            assertNotNull(result);
            verify(analystRepository, times(1)).getAnalystByLogin("john_analyst");
        }
    }

    @Nested
    @DisplayName("updateAnalyst() tests")
    class UpdateAnalystTests {

        @Test
        @DisplayName("Should update analyst successfully")
        void shouldUpdateAnalystSuccessfully() {
            // given
            when(analystRepository.findById(1)).thenReturn(Optional.of(testAnalyst));
            when(analystMapper.toFullInfoDTO(testAnalyst)).thenReturn(testAnalystInfoDTO);

            // when
            AnalystInfoDTO result = analystService.updateAnalyst(1, updateDTO);

            // then
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("deleteAnalyst() tests")
    class DeleteAnalystTests {

        @Test
        @DisplayName("Should delete analyst by id")
        void shouldDeleteAnalystById() {
            // given
            when(analystRepository.findById(1)).thenReturn(Optional.of(testAnalyst));
            doNothing().when(analystRepository).deleteById(1);

            // when
            analystService.deleteAnalyst(1);

            // then
            verify(analystRepository, times(1)).deleteById(1);
        }
    }
}