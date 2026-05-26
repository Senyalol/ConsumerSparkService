package com.bankSpark.analyticsService.segment;

import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.ORM.segment.SegmentUser;
import com.bankSpark.analyticsService.repository.SegmentURepository;
import com.bankSpark.analyticsService.service.segments.SegmentServiceImpl;
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
@DisplayName("SegmentServiceImpl Unit Tests")
class SegmentServiceImplTest {

    @Mock
    private SegmentURepository segmentURepository;

    @InjectMocks
    private SegmentServiceImpl segmentService;

    private User testUser1;
    private User testUser2;
    private SegmentUser testSegment1;
    private SegmentUser testSegment2;
    private SegmentUser testSegment3;

    @BeforeEach
    void setUp() {
        testUser1 = new User();
        testUser1.setId(1);
        testUser1.setFirstname("John");
        testUser1.setLastname("Doe");

        testUser2 = new User();
        testUser2.setId(2);
        testUser2.setFirstname("Jane");
        testUser2.setLastname("Smith");

        testSegment1 = new SegmentUser();
        testSegment1.setId(1);
        testSegment1.setUser(testUser1);
        testSegment1.setSegment("VIP");
        testSegment1.setRMinutes(1.5);
        testSegment1.setF(10L);
        testSegment1.setM(5000.0);

        testSegment2 = new SegmentUser();
        testSegment2.setId(2);
        testSegment2.setUser(testUser2);
        testSegment2.setSegment("Active");
        testSegment2.setRMinutes(5.0);
        testSegment2.setF(5L);
        testSegment2.setM(2000.0);

        testSegment3 = new SegmentUser();
        testSegment3.setId(3);
        testSegment3.setUser(testUser1);
        testSegment3.setSegment("Newbie");
        testSegment3.setRMinutes(0.5);
        testSegment3.setF(1L);
        testSegment3.setM(100.0);
    }

    @Nested
    @DisplayName("getAllSegments() tests")
    class GetAllSegmentsTests {

        @Test
        @DisplayName("Should return all segments")
        void shouldReturnAllSegments() {
            // given
            List<SegmentUser> expectedSegments = Arrays.asList(testSegment1, testSegment2);
            when(segmentURepository.findAll()).thenReturn(expectedSegments);

            // when
            List<SegmentUser> result = segmentService.getAllSegments();

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(segmentURepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no segments exist")
        void shouldReturnEmptyListWhenNoSegments() {
            // given
            when(segmentURepository.findAll()).thenReturn(Collections.emptyList());

            // when
            List<SegmentUser> result = segmentService.getAllSegments();

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(segmentURepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getSegmentById() tests")
    class GetSegmentByIdTests {

        @Test
        @DisplayName("Should return segment when id exists")
        void shouldReturnSegmentWhenIdExists() {
            // given
            when(segmentURepository.findById(1)).thenReturn(Optional.of(testSegment1));

            // when
            SegmentUser result = segmentService.getSegmentById(1);

            // then
            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("VIP", result.getSegment());
            verify(segmentURepository, times(1)).findById(1);
        }

        @Test
        @DisplayName("Should throw exception when id does not exist")
        void shouldThrowExceptionWhenIdDoesNotExist() {
            // given
            when(segmentURepository.findById(999)).thenReturn(Optional.empty());

            // when & then
            assertThrows(Exception.class, () -> segmentService.getSegmentById(999));
            verify(segmentURepository, times(1)).findById(999);
        }
    }

    @Nested
    @DisplayName("getSegmentsByUser() - by userId tests")
    class GetSegmentsByUserIdTests {

        @Test
        @DisplayName("Should return segments for specific user ID")
        void shouldReturnSegmentsForUserId() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByUser(1);

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(s -> s.getUser().getId().equals(1)));
            verify(segmentURepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when user has no segments")
        void shouldReturnEmptyListWhenUserHasNoSegments() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByUser(999);

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(segmentURepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getSegmentsByUser() - by lastName tests")
    class GetSegmentsByLastNameTests {

        @Test
        @DisplayName("Should return segments for specific last name")
        void shouldReturnSegmentsForLastName() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByUser("Doe");

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(s -> s.getUser().getLastname().equals("Doe")));
            verify(segmentURepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when last name not found")
        void shouldReturnEmptyListWhenLastNameNotFound() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByUser("NonExistent");

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(segmentURepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getCertainSegments() tests")
    class GetCertainSegmentsTests {

        @Test
        @DisplayName("Should return segments by segment type")
        void shouldReturnSegmentsBySegmentType() {
            // given
            List<SegmentUser> expectedSegments = Arrays.asList(testSegment1);
            when(segmentURepository.findBySegment("VIP")).thenReturn(expectedSegments);

            // when
            List<SegmentUser> result = segmentService.getCertainSegments("VIP");

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("VIP", result.get(0).getSegment());
            verify(segmentURepository, times(1)).findBySegment("VIP");
        }
    }

    @Nested
    @DisplayName("R-Metric filters tests")
    class RMetricTests {

        @Test
        @DisplayName("Should return segments with R > threshold")
        void shouldReturnSegmentsWithRMore() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByRMore(2.0);

            // then
            assertNotNull(result);
            // Проверяем, что все элементы имеют RMinutes >= 2.0
            assertTrue(result.stream().allMatch(s -> s.getRMinutes() >= 2.0));
            assertEquals(1, result.size());
            assertEquals(5.0, result.get(0).getRMinutes());
        }

        @Test
        @DisplayName("Should return segments with R < threshold")
        void shouldReturnSegmentsWithRLess() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            // Ваш метод использует <, не <=
            List<SegmentUser> result = segmentService.getSegmentsByRLess(2.0);

            // then
            assertNotNull(result);
            // Проверяем, что все элементы имеют RMinutes < 2.0
            assertTrue(result.stream().allMatch(s -> s.getRMinutes() < 2.0));
            // Должно быть 2 сегмента: testSegment1 (1.5) и testSegment3 (0.5)
            assertEquals(2, result.size());
        }
        @Test
        @DisplayName("Should return segments within R range")
        void shouldReturnSegmentsWithinRRange() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByRRange(1.0, 3.0);

            // then
            assertNotNull(result);
            assertTrue(result.stream().allMatch(s -> s.getRMinutes() >= 1.0 && s.getRMinutes() <= 3.0));
            assertEquals(1, result.size());
            assertEquals(1.5, result.get(0).getRMinutes());
        }
    }

    @Nested
    @DisplayName("F-Metric filters tests")
    class FMetricTests {

        @Test
        @DisplayName("Should return segments with F > threshold")
        void shouldReturnSegmentsWithFMore() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByFMore(5L);

            // then
            assertNotNull(result);
            // Ваш метод использует >=, поэтому результат: F >= 5
            assertTrue(result.stream().allMatch(s -> s.getF() >= 5L));
            assertEquals(2, result.size()); // testSegment1 (10) и testSegment2 (5)
        }

        @Test
        @DisplayName("Should return segments with F < threshold")
        void shouldReturnSegmentsWithFLess() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            // Ваш метод использует <=
            List<SegmentUser> result = segmentService.getSegmentsByFLess(5L);

            // then
            assertNotNull(result);
            // Проверяем, что все элементы имеют F <= 5
            assertTrue(result.stream().allMatch(s -> s.getF() <= 5L));
            assertEquals(2, result.size()); // testSegment2 (5) и testSegment3 (1)
        }

        @Test
        @DisplayName("Should return segments within F range")
        void shouldReturnSegmentsWithinFRange() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByFRange(5L, 10L);

            // then
            assertNotNull(result);
            assertTrue(result.stream().allMatch(s -> s.getF() >= 5L && s.getF() <= 10L));
            assertEquals(2, result.size()); // testSegment1 (10) и testSegment2 (5)
        }
    }

    @Nested
    @DisplayName("M-Metric filters tests")
    class MMetricTests {

        @Test
        @DisplayName("Should return segments with M > threshold")
        void shouldReturnSegmentsWithMMore() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByMMore(3000.0);

            // then
            assertNotNull(result);
            assertTrue(result.stream().allMatch(s -> s.getM() >= 3000.0));
            assertEquals(1, result.size());
            assertEquals(5000.0, result.get(0).getM());
        }

        @Test
        @DisplayName("Should return segments with M < threshold")
        void shouldReturnSegmentsWithMLess() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByMLess(1000.0);

            // then
            assertNotNull(result);
            assertTrue(result.stream().allMatch(s -> s.getM() <= 1000.0));
            assertEquals(1, result.size());
            assertEquals(100.0, result.get(0).getM());
        }

        @Test
        @DisplayName("Should return segments within M range")
        void shouldReturnSegmentsWithinMRange() {
            // given
            List<SegmentUser> allSegments = Arrays.asList(testSegment1, testSegment2, testSegment3);
            when(segmentURepository.findAll()).thenReturn(allSegments);

            // when
            List<SegmentUser> result = segmentService.getSegmentsByMRange(1000.0, 3000.0);

            // then
            assertNotNull(result);
            assertTrue(result.stream().allMatch(s -> s.getM() >= 1000.0 && s.getM() <= 3000.0));
            assertEquals(1, result.size());
            assertEquals(2000.0, result.get(0).getM());
        }
    }
}