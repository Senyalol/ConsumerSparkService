package com.bankSpark.analyticsService.segment;

import com.bankSpark.analyticsService.DTO.segmentsRFM.KafkaSegmentUserDTO;
import com.bankSpark.analyticsService.DTO.segmentsRFM.SegmentUserDTO;
import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.ORM.segment.SegmentUser;
import com.bankSpark.analyticsService.mapper.SegmentUMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SegmentUMapper Unit Tests")
class SegmentUMapperTest {

    private SegmentUMapper segmentUMapper;
    private User testUser;
    private SegmentUser testSegmentUser;

    @BeforeEach
    void setUp() {
        segmentUMapper = new SegmentUMapper();

        testUser = new User();
        testUser.setId(1);
        testUser.setFirstname("John");
        testUser.setLastname("Doe");

        testSegmentUser = new SegmentUser();
        testSegmentUser.setId(100);
        testSegmentUser.setUser(testUser);
        testSegmentUser.setSegment("VIP");
        testSegmentUser.setRMinutes(1.5);
        testSegmentUser.setF(10L);
        testSegmentUser.setM(5000.0);
        testSegmentUser.setUpdatedAt(1234567890L);
    }

    @Nested
    @DisplayName("toDTO() tests")
    class ToDTOTests {

        @Test
        @DisplayName("Should convert SegmentUser entity to SegmentUserDTO successfully")
        void shouldConvertSegmentUserToDTO() {
            // given
            testSegmentUser.setUpdatedAt(1234567890L);

            // when
            SegmentUserDTO result = segmentUMapper.toDTO(testSegmentUser);

            // then
            assertNotNull(result);
            assertEquals(100, result.getUSegmentId());
            assertEquals(1, result.getUserId());
            assertEquals("VIP", result.getSegment());
            assertEquals(1.5, result.getRMinutes());
            assertEquals(10L, result.getF());
            assertEquals(5000.0, result.getM());
            assertNotNull(result.getUpdatedAt());
        }

        @Test
        @DisplayName("Should handle null segment user gracefully")
        void shouldHandleNullSegmentUser() {
            // when & then
            assertThrows(NullPointerException.class, () -> segmentUMapper.toDTO(null));
        }
    }

    @Nested
    @DisplayName("toListDTO() tests")
    class ToListDTOTests {

        @Test
        @DisplayName("Should convert list of SegmentUsers to list of DTOs")
        void shouldConvertListOfSegmentUsersToListOfDTOs() {
            // given
            List<SegmentUser> segmentUsers = Arrays.asList(testSegmentUser);

            // when
            List<SegmentUserDTO> result = segmentUMapper.toListDTO(segmentUsers);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(100, result.get(0).getUSegmentId());
            assertEquals("VIP", result.get(0).getSegment());
        }

        @Test
        @DisplayName("Should return empty list for empty input list")
        void shouldReturnEmptyListForEmptyInput() {
            // given
            List<SegmentUser> segmentUsers = Collections.emptyList();

            // when
            List<SegmentUserDTO> result = segmentUMapper.toListDTO(segmentUsers);

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("fromKafkaDTOtoEntity() tests")
    class FromKafkaDTOToEntityTests {

        @Test
        @DisplayName("Should convert Kafka DTO to SegmentUser entity successfully")
        void shouldConvertKafkaDTOToEntity() {
            // given
            KafkaSegmentUserDTO kafkaDTO = new KafkaSegmentUserDTO();
            kafkaDTO.setSegment("VIP");
            kafkaDTO.setR_minutes(1.5);
            kafkaDTO.setF(10L);
            kafkaDTO.setM(5000.0);
            kafkaDTO.setUpdated_at(1234567890L);

            // when
            SegmentUser result = segmentUMapper.fromKafkaDTOtoEntity(kafkaDTO, testUser);

            // then
            assertNotNull(result);
            assertEquals(testUser, result.getUser());
            assertEquals("VIP", result.getSegment());
            assertEquals(1.5, result.getRMinutes());
            assertEquals(10L, result.getF());
            assertEquals(5000.0, result.getM());
            assertEquals(1234567890L, result.getUpdatedAt());
        }

        @Test
        @DisplayName("Should handle null Kafka DTO gracefully")
        void shouldHandleNullKafkaDTO() {
            // when & then
            assertThrows(NullPointerException.class,
                    () -> segmentUMapper.fromKafkaDTOtoEntity(null, testUser));
        }
    }
}