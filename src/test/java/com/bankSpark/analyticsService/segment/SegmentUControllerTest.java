package com.bankSpark.analyticsService.segment;

import com.bankSpark.analyticsService.DTO.segmentsRFM.SegmentUserDTO;
import com.bankSpark.analyticsService.controller.SegmentUController;
import com.bankSpark.analyticsService.facade.segments.SegmentUFacade;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SegmentUController Unit Tests")
class SegmentUControllerTest {

    @Mock
    private SegmentUFacade segmentUFacade;

    @InjectMocks
    private SegmentUController segmentUController;

    private SegmentUserDTO testSegmentDTO1;
    private SegmentUserDTO testSegmentDTO2;

    @BeforeEach
    void setUp() {
        testSegmentDTO1 = new SegmentUserDTO();
        testSegmentDTO1.setUSegmentId(1);
        testSegmentDTO1.setUserId(1);
        testSegmentDTO1.setSegment("VIP");
        testSegmentDTO1.setRMinutes(1.5);
        testSegmentDTO1.setF(10L);
        testSegmentDTO1.setM(5000.0);

        testSegmentDTO2 = new SegmentUserDTO();
        testSegmentDTO2.setUSegmentId(2);
        testSegmentDTO2.setUserId(2);
        testSegmentDTO2.setSegment("Active");
        testSegmentDTO2.setRMinutes(5.0);
        testSegmentDTO2.setF(5L);
        testSegmentDTO2.setM(2000.0);
    }

    @Nested
    @DisplayName("getAllSegments() tests")
    class GetAllSegmentsTests {

        @Test
        @DisplayName("Should return 200 OK with list of segments")
        void shouldReturn200OkWithSegmentsList() {
            // given
            List<SegmentUserDTO> expectedSegments = Arrays.asList(testSegmentDTO1, testSegmentDTO2);
            when(segmentUFacade.getAllSegments()).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getAllSegments();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(segmentUFacade, times(1)).getAllSegments();
        }

        @Test
        @DisplayName("Should return 204 No Content when no segments exist")
        void shouldReturn204NoContentWhenNoSegments() {
            // given
            when(segmentUFacade.getAllSegments()).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getAllSegments();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(segmentUFacade, times(1)).getAllSegments();
        }
    }

    @Nested
    @DisplayName("getSegmentById() tests")
    class GetSegmentByIdTests {

        @Test
        @DisplayName("Should return 200 OK when valid id provided")
        void shouldReturn200OkWhenValidId() {
            // given
            when(segmentUFacade.getSegmentById(1)).thenReturn(testSegmentDTO1);

            // when
            ResponseEntity<SegmentUserDTO> response = segmentUController.getSegmentById(1);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().getUSegmentId());
            verify(segmentUFacade, times(1)).getSegmentById(1);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when id is invalid")
        void shouldReturn400BadRequestWhenIdInvalid() {
            // given
            when(segmentUFacade.getSegmentById(0)).thenReturn(null);

            // when
            ResponseEntity<SegmentUserDTO> response = segmentUController.getSegmentById(0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            verify(segmentUFacade, times(1)).getSegmentById(0);
        }

        @Test
        @DisplayName("Should return 204 No Content when segment not found")
        void shouldReturn204NoContentWhenSegmentNotFound() {
            // given
            when(segmentUFacade.getSegmentById(999)).thenReturn(null);

            // when
            ResponseEntity<SegmentUserDTO> response = segmentUController.getSegmentById(999);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(segmentUFacade, times(1)).getSegmentById(999);
        }
    }

    @Nested
    @DisplayName("getSegmentsBySegmentId() - type endpoint tests")
    class GetSegmentsBySegmentTypeTests {

        @Test
        @DisplayName("Should return 200 OK when segments exist for type")
        void shouldReturn200OkWhenSegmentsExist() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO1);
            // ✅ Ожидаем 2 вызова (один в if, один в return)
            when(segmentUFacade.getCertainSegments("VIP")).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getSegmentsBySegmentId("VIP");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            // ✅ Проверяем 2 вызова
            verify(segmentUFacade, times(2)).getCertainSegments("VIP");
        }

        @Test
        @DisplayName("Should return 204 No Content when no segments for type")
        void shouldReturn204NoContentWhenNoSegments() {
            // given
            when(segmentUFacade.getCertainSegments("NonExistent")).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getSegmentsBySegmentId("NonExistent");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            // ✅ Проверяем 1 вызов (только в if, return не выполняется)
            verify(segmentUFacade, times(1)).getCertainSegments("NonExistent");
        }
    }

    @Nested
    @DisplayName("R-Metric endpoints tests")
    class RMetricEndpointTests {

        @Test
        @DisplayName("getRMoreSegments - Should return 200 OK")
        void shouldReturn200OkForRMore() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO2);
            when(segmentUFacade.getSegmentsByRMore(3.0)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getRMoreSegments(3.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            // ✅ Проверяем 2 вызова (один в if, один в return)
            verify(segmentUFacade, times(2)).getSegmentsByRMore(3.0);
        }

        @Test
        @DisplayName("getRLessSegments - Should return 200 OK")
        void shouldReturn200OkForRLess() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO1);
            when(segmentUFacade.getSegmentsByRLess(2.0)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getRLessSegments(2.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(segmentUFacade, times(2)).getSegmentsByRLess(2.0);
        }

        @Test
        @DisplayName("getRRangeSegments - Should return 200 OK")
        void shouldReturn200OkForRRange() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO1);
            when(segmentUFacade.getSegmentsByRRange(1.0, 2.0)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getRRangeSegments(1.0, 2.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(segmentUFacade, times(2)).getSegmentsByRRange(1.0, 2.0);
        }

        @Test
        @DisplayName("R endpoints should return 204 No Content when empty")
        void shouldReturn204NoContentWhenEmpty() {
            // given
            when(segmentUFacade.getSegmentsByRMore(3.0)).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getRMoreSegments(3.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            // ✅ Проверяем 1 вызов (только в if)
            verify(segmentUFacade, times(1)).getSegmentsByRMore(3.0);
        }
    }

    @Nested
    @DisplayName("F-Metric endpoints tests")
    class FMetricEndpointTests {

        @Test
        @DisplayName("getFMoreSegments - Should return 200 OK")
        void shouldReturn200OkForFMore() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO1);
            when(segmentUFacade.getSegmentsByFMore(5L)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getFMoreSegments(5L);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(segmentUFacade, times(2)).getSegmentsByFMore(5L);
        }

        @Test
        @DisplayName("getFLessSegments - Should return 200 OK")
        void shouldReturn200OkForFLess() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO2);
            when(segmentUFacade.getSegmentsByFLess(6L)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getFLessSegments(6L);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(segmentUFacade, times(2)).getSegmentsByFLess(6L);
        }

        @Test
        @DisplayName("getFRangeSegments - Should return 200 OK")
        void shouldReturn200OkForFRange() {
            // given
            List<SegmentUserDTO> expectedSegments = Arrays.asList(testSegmentDTO1, testSegmentDTO2);
            when(segmentUFacade.getSegmentsByFRange(5L, 10L)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getFRangeSegments(5L, 10L);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(segmentUFacade, times(2)).getSegmentsByFRange(5L, 10L);
        }
    }

    @Nested
    @DisplayName("M-Metric endpoints tests")
    class MMetricEndpointTests {

        @Test
        @DisplayName("getMMoreSegments - Should return 200 OK")
        void shouldReturn200OkForMMore() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO1);
            when(segmentUFacade.getSegmentsByMMore(3000.0)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getMMoreSegments(3000.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(segmentUFacade, times(2)).getSegmentsByMMore(3000.0);
        }

        @Test
        @DisplayName("getMLessSegments - Should return 200 OK")
        void shouldReturn200OkForMLess() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO2);
            when(segmentUFacade.getSegmentsByMLess(3000.0)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getMLessSegments(3000.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(segmentUFacade, times(2)).getSegmentsByMLess(3000.0);
        }

        @Test
        @DisplayName("getMRangeSegments - Should return 200 OK")
        void shouldReturn200OkForMRange() {
            // given
            List<SegmentUserDTO> expectedSegments = Collections.singletonList(testSegmentDTO2);
            when(segmentUFacade.getSegmentsByMRange(1000.0, 3000.0)).thenReturn(expectedSegments);

            // when
            ResponseEntity<List<SegmentUserDTO>> response = segmentUController.getMRangeSegments(1000.0, 3000.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(segmentUFacade, times(2)).getSegmentsByMRange(1000.0, 3000.0);
        }
    }
}