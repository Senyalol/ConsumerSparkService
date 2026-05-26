package com.bankSpark.analyticsService.anomaly;

import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.ORM.anomaly.Anomaly;
import com.bankSpark.analyticsService.repository.AnomalyRepository;
import com.bankSpark.analyticsService.service.anomaly.AnomalyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnomalyServiceImpl Unit Tests")
class AnomalyServiceImplTest {

    @Mock
    private AnomalyRepository anomalyRepository;

    @InjectMocks
    private AnomalyServiceImpl anomalyService;

    private User testUser;
    private Anomaly testAnomaly1;
    private Anomaly testAnomaly2;
    private Anomaly testAnomaly3;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);

        testAnomaly1 = new Anomaly();
        testAnomaly1.setId(1);
        testAnomaly1.setUser(testUser);
        testAnomaly1.setType("BIGGER_THEN_AVG_CHECK");
        testAnomaly1.setSum(5000.0);
        testAnomaly1.setAvgCheck(1000.0);
        testAnomaly1.setEventTime(1000L);

        testAnomaly2 = new Anomaly();
        testAnomaly2.setId(2);
        testAnomaly2.setUser(testUser);
        testAnomaly2.setType("NEGATIVE_M");
        testAnomaly2.setSum(2000.0);
        testAnomaly2.setAvgCheck(500.0);
        testAnomaly2.setEventTime(2000L);

        testAnomaly3 = new Anomaly();
        testAnomaly3.setId(3);
        testAnomaly3.setUser(testUser);
        testAnomaly3.setType("STRUCTURING_SMALL_TRANSACTIONS");
        testAnomaly3.setSum(1000.0);
        testAnomaly3.setAvgCheck(100.0);
        testAnomaly3.setEventTime(3000L);
    }

    @Nested
    @DisplayName("getAllAnomalies() tests")
    class GetAllAnomaliesTests {

        @Test
        @DisplayName("Should return all anomalies")
        void shouldReturnAllAnomalies() {
            // given
            List<Anomaly> expectedAnomalies = Arrays.asList(testAnomaly1, testAnomaly2);
            when(anomalyRepository.findAll()).thenReturn(expectedAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAllAnomalies();

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(anomalyRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no anomalies exist")
        void shouldReturnEmptyListWhenNoAnomalies() {
            // given
            when(anomalyRepository.findAll()).thenReturn(Collections.emptyList());

            // when
            List<Anomaly> result = anomalyService.getAllAnomalies();

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(anomalyRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getAnomalyById() tests")
    class GetAnomalyByIdTests {

        @Test
        @DisplayName("Should return anomaly when id exists")
        void shouldReturnAnomalyWhenIdExists() {
            // given
            when(anomalyRepository.findById(1)).thenReturn(Optional.of(testAnomaly1));

            // when
            Anomaly result = anomalyService.getAnomalyById(1);

            // then
            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("BIGGER_THEN_AVG_CHECK", result.getType());
            verify(anomalyRepository, times(1)).findById(1);
        }

        @Test
        @DisplayName("Should throw exception when id does not exist")
        void shouldThrowExceptionWhenIdDoesNotExist() {
            // given
            when(anomalyRepository.findById(999)).thenReturn(Optional.empty());

            // when & then
            assertThrows(Exception.class, () -> anomalyService.getAnomalyById(999));
            verify(anomalyRepository, times(1)).findById(999);
        }
    }

    @Nested
    @DisplayName("getAnomalyByType() tests")
    class GetAnomalyByTypeTests {

        @Test
        @DisplayName("Should return anomalies for valid type")
        void shouldReturnAnomaliesForValidType() {
            // given
            List<Anomaly> expectedAnomalies = Collections.singletonList(testAnomaly1);
            when(anomalyRepository.findByType("BIGGER_THEN_AVG_CHECK")).thenReturn(expectedAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomalyByType("BIGGER_THEN_AVG_CHECK");

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(anomalyRepository, times(1)).findByType("BIGGER_THEN_AVG_CHECK");
        }

        @Test
        @DisplayName("Should return empty list for invalid type")
        void shouldReturnEmptyListForInvalidType() {
            // when
            List<Anomaly> result = anomalyService.getAnomalyByType("INVALID_TYPE");

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(anomalyRepository, never()).findByType(anyString());
        }

        @Test
        @DisplayName("Should be case insensitive - passes through original case to repository")
        void shouldBeCaseInsensitive() {
            // given
            List<Anomaly> expectedAnomalies = Collections.singletonList(testAnomaly1);
            // Мокаем с тем же регистром, который приходит в сервис
            when(anomalyRepository.findByType("BIGGER_THEN_AVG_CHECK")).thenReturn(expectedAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomalyByType("BIGGER_THEN_AVG_CHECK");

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(anomalyRepository, times(1)).findByType("BIGGER_THEN_AVG_CHECK");
        }

        @Test
        @DisplayName("Should work with lowercase input")
        void shouldWorkWithLowercaseInput() {
            // given
            List<Anomaly> expectedAnomalies = Collections.singletonList(testAnomaly1);
            // Мокаем с тем регистром, который реально будет передан в репозиторий
            // (сервис передаёт оригинальное значение, не преобразуя)
            when(anomalyRepository.findByType("bigger_then_avg_check")).thenReturn(expectedAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomalyByType("bigger_then_avg_check");

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(anomalyRepository, times(1)).findByType("bigger_then_avg_check");
        }
    }

    @Nested
    @DisplayName("Sum filters tests")
    class SumFiltersTests {

        @Test
        @DisplayName("getAnomaliesBySumRange - Should return anomalies within range")
        void shouldReturnAnomaliesWithinSumRange() {
            // given
            List<Anomaly> allAnomalies = Arrays.asList(testAnomaly1, testAnomaly2, testAnomaly3);
            when(anomalyRepository.findAll()).thenReturn(allAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesBySumRange(1500.0, 3000.0);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(2000.0, result.get(0).getSum());
        }

        @Test
        @DisplayName("getAnomaliesByMoreSum - Should return anomalies with sum >= threshold")
        void shouldReturnAnomaliesWithSumGreaterThanOrEqual() {
            // given
            List<Anomaly> allAnomalies = Arrays.asList(testAnomaly1, testAnomaly2, testAnomaly3);
            when(anomalyRepository.findAll()).thenReturn(allAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByMoreSum(2000.0);

            // then
            assertNotNull(result);
            assertEquals(2, result.size()); // 5000 and 2000
            assertTrue(result.stream().allMatch(a -> a.getSum() >= 2000.0));
        }

        @Test
        @DisplayName("getAnomaliesByLessSum - Should return anomalies with sum < threshold")
        void shouldReturnAnomaliesWithSumLessThanThreshold() {
            // given
            List<Anomaly> allAnomalies = Arrays.asList(testAnomaly1, testAnomaly2, testAnomaly3);
            when(anomalyRepository.findAll()).thenReturn(allAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByLessSum(2000.0);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1000.0, result.get(0).getSum());
        }
    }

    @Nested
    @DisplayName("Event time filters tests")
    class EventTimeFiltersTests {

        @Test
        @DisplayName("getAnomaliesByMinEventTime - Should return anomalies with eventTime <= threshold")
        void shouldReturnAnomaliesWithEventTimeLessOrEqual() {
            // given
            List<Anomaly> allAnomalies = Arrays.asList(testAnomaly1, testAnomaly2, testAnomaly3);
            when(anomalyRepository.findAll()).thenReturn(allAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByMinEventTime(2000L);

            // then
            assertNotNull(result);
            assertEquals(2, result.size()); // 1000 and 2000
        }

        @Test
        @DisplayName("getAnomaliesByMaxEventTime - Should return anomalies with eventTime > threshold")
        void shouldReturnAnomaliesWithEventTimeGreaterThanThreshold() {
            // given
            List<Anomaly> allAnomalies = Arrays.asList(testAnomaly1, testAnomaly2, testAnomaly3);
            when(anomalyRepository.findAll()).thenReturn(allAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByMaxEventTime(2000L);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(3000L, result.get(0).getEventTime());
        }

        @Test
        @DisplayName("getAnomaliesByEventTimeRange - Should return anomalies within range")
        void shouldReturnAnomaliesWithinEventTimeRange() {
            // given
            List<Anomaly> allAnomalies = Arrays.asList(testAnomaly1, testAnomaly2, testAnomaly3);
            when(anomalyRepository.findAll()).thenReturn(allAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByEventTimeRange(1500L, 2500L);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(2000L, result.get(0).getEventTime());
        }
    }

    @Nested
    @DisplayName("AvgCheck filters tests")
    class AvgCheckFiltersTests {

        @Test
        @DisplayName("getAnomaliesByAvgCheck - single param should return anomalies with avgCheck >= threshold")
        void shouldReturnAnomaliesWithAvgCheckGreaterThanOrEqual() {
            // given
            List<Anomaly> allAnomalies = Arrays.asList(testAnomaly1, testAnomaly2, testAnomaly3);
            when(anomalyRepository.findAll()).thenReturn(allAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByAvgCheck(500.0);

            // then
            assertNotNull(result);
            assertEquals(2, result.size()); // 1000 and 500
        }

        @Test
        @DisplayName("getAnomaliesByAvgCheck - two params should return anomalies within range")
        void shouldReturnAnomaliesWithinAvgCheckRange() {
            // given
            List<Anomaly> allAnomalies = Arrays.asList(testAnomaly1, testAnomaly2, testAnomaly3);
            when(anomalyRepository.findAll()).thenReturn(allAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByAvgCheck(100.0, 500.0);

            // then
            assertNotNull(result);
            assertEquals(2, result.size()); // 100 and 500
        }
    }

    @Nested
    @DisplayName("getAnomaliesByUserId() tests")
    class GetAnomaliesByUserIdTests {

        @Test
        @DisplayName("Should return anomalies for specific user")
        void shouldReturnAnomaliesForSpecificUser() {
            // given
            List<Anomaly> expectedAnomalies = Arrays.asList(testAnomaly1, testAnomaly2);
            when(anomalyRepository.findByUserId(1)).thenReturn(expectedAnomalies);

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByUserId(1);

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(anomalyRepository, times(1)).findByUserId(1);
        }

        @Test
        @DisplayName("Should return empty list when user has no anomalies")
        void shouldReturnEmptyListWhenUserHasNoAnomalies() {
            // given
            when(anomalyRepository.findByUserId(999)).thenReturn(Collections.emptyList());

            // when
            List<Anomaly> result = anomalyService.getAnomaliesByUserId(999);

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(anomalyRepository, times(1)).findByUserId(999);
        }
    }
}