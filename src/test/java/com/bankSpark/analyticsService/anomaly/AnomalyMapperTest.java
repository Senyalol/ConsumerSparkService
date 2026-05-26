package com.bankSpark.analyticsService.anomaly;

import com.bankSpark.analyticsService.DTO.anomaly.AnomalyDTO;
import com.bankSpark.analyticsService.DTO.anomaly.KafkaAnomalyDTO;
import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.ORM.anomaly.Anomaly;
import com.bankSpark.analyticsService.mapper.AnomalyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnomalyMapper Unit Tests")
class AnomalyMapperTest {

    private AnomalyMapper anomalyMapper;
    private User testUser;
    private Anomaly testAnomaly;

    @BeforeEach
    void setUp() {
        anomalyMapper = new AnomalyMapper();

        testUser = new User();
        testUser.setId(1);
        testUser.setFirstname("John");
        testUser.setLastname("Doe");

        testAnomaly = new Anomaly();
        testAnomaly.setId(100);
        testAnomaly.setUser(testUser);
        testAnomaly.setEventTime(1234567890L);
        testAnomaly.setType("BIGGER_THEN_AVG_CHECK");
        testAnomaly.setSum(5000.0);
        testAnomaly.setAvgCheck(1000.0);
        testAnomaly.setMessage("Test anomaly message");
    }

    @Nested
    @DisplayName("toDTO() tests")
    class ToDTOTests {

        @Test
        @DisplayName("Should convert Anomaly entity to AnomalyDTO successfully")
        void shouldConvertAnomalyToDTO() {
            // given
            testAnomaly.setEventTime(1234567890000L); // миллисекунды

            // when
            AnomalyDTO result = anomalyMapper.toDTO(testAnomaly);

            // then
            assertNotNull(result);
            assertEquals(100, result.getAnomalyId());
            assertEquals(1, result.getUserId());
            assertEquals("BIGGER_THEN_AVG_CHECK", result.getType());
            assertEquals(5000.0, result.getSum());
            assertEquals(1000.0, result.getAvgCheck());
            assertEquals("Test anomaly message", result.getMessage());
            assertNotNull(result.getEventTime());
        }

        @Test
        @DisplayName("Should handle null event time")
        void shouldHandleNullEventTime() {
            // given
            testAnomaly.setEventTime(0L);

            // when
            AnomalyDTO result = anomalyMapper.toDTO(testAnomaly);

            // then
            assertNotNull(result);
            assertNull(result.getEventTime());
        }

        @Test
        @DisplayName("Should handle null anomaly")
        void shouldHandleNullAnomaly() {
            // when & then
            assertThrows(NullPointerException.class, () -> anomalyMapper.toDTO(null));
        }
    }

    @Nested
    @DisplayName("toListDTO() tests")
    class ToListDTOTests {

        @Test
        @DisplayName("Should convert list of Anomalies to list of DTOs")
        void shouldConvertListOfAnomaliesToListOfDTOs() {
            // given
            List<Anomaly> anomalies = Arrays.asList(testAnomaly);

            // when
            List<AnomalyDTO> result = anomalyMapper.toListDTO(anomalies);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(100, result.get(0).getAnomalyId());
            assertEquals("BIGGER_THEN_AVG_CHECK", result.get(0).getType());
        }

        @Test
        @DisplayName("Should return empty list for empty input")
        void shouldReturnEmptyListForEmptyInput() {
            // given
            List<Anomaly> anomalies = Collections.emptyList();

            // when
            List<AnomalyDTO> result = anomalyMapper.toListDTO(anomalies);

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("fromKafkaDTOtoEntity() tests")
    class FromKafkaDTOToEntityTests {

        @Test
        @DisplayName("Should convert Kafka DTO to Anomaly entity successfully")
        void shouldConvertKafkaDTOToEntity() {
            // given
            KafkaAnomalyDTO kafkaDTO = new KafkaAnomalyDTO();
            kafkaDTO.setEvent_time(1234567890L);
            kafkaDTO.setType("BIGGER_THEN_AVG_CHECK");
            kafkaDTO.setSum(5000.0);
            kafkaDTO.setAvg_check_5min(1000.0);
            kafkaDTO.setMessage("Test anomaly");

            // when
            Anomaly result = anomalyMapper.fromKafkaDTOtoEntity(kafkaDTO, testUser);

            // then
            assertNotNull(result);
            assertEquals(testUser, result.getUser());
            assertEquals(1234567890L, result.getEventTime());
            assertEquals("BIGGER_THEN_AVG_CHECK", result.getType());
            assertEquals(5000.0, result.getSum());
            assertEquals(1000.0, result.getAvgCheck());
            assertEquals("Test anomaly", result.getMessage());
        }

        @Test
        @DisplayName("Should handle null Kafka DTO")
        void shouldHandleNullKafkaDTO() {
            // when & then
            assertThrows(NullPointerException.class,
                    () -> anomalyMapper.fromKafkaDTOtoEntity(null, testUser));
        }
    }
}