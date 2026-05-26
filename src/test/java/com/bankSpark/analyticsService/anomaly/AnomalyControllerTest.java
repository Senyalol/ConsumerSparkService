package com.bankSpark.analyticsService.anomaly;

import com.bankSpark.analyticsService.DTO.anomaly.AnomalyDTO;
import com.bankSpark.analyticsService.controller.AnomalyController;
import com.bankSpark.analyticsService.facade.anomaly.AnomalyFacade;
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
@DisplayName("AnomalyController Unit Tests")
class AnomalyControllerTest {

    @Mock
    private AnomalyFacade anomalyFacade;

    @InjectMocks
    private AnomalyController anomalyController;

    private AnomalyDTO testAnomalyDTO1;
    private AnomalyDTO testAnomalyDTO2;

    @BeforeEach
    void setUp() {
        testAnomalyDTO1 = new AnomalyDTO();
        testAnomalyDTO1.setAnomalyId(1);
        testAnomalyDTO1.setUserId(1);
        testAnomalyDTO1.setType("BIGGER_THEN_AVG_CHECK");
        testAnomalyDTO1.setSum(5000.0);
        testAnomalyDTO1.setAvgCheck(1000.0);
        testAnomalyDTO1.setMessage("Test message 1");

        testAnomalyDTO2 = new AnomalyDTO();
        testAnomalyDTO2.setAnomalyId(2);
        testAnomalyDTO2.setUserId(2);
        testAnomalyDTO2.setType("NEGATIVE_M");
        testAnomalyDTO2.setSum(2000.0);
        testAnomalyDTO2.setAvgCheck(500.0);
        testAnomalyDTO2.setMessage("Test message 2");
    }

    @Nested
    @DisplayName("getAllAnomalies() tests")
    class GetAllAnomaliesTests {

        @Test
        @DisplayName("Should return 200 OK with list of anomalies")
        void shouldReturn200OkWithAnomaliesList() {
            // given
            List<AnomalyDTO> expectedAnomalies = Arrays.asList(testAnomalyDTO1, testAnomalyDTO2);
            when(anomalyFacade.getAllAnomalies()).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAllAnomalies();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(anomalyFacade, times(1)).getAllAnomalies();
        }

        @Test
        @DisplayName("Should return 204 No Content when no anomalies exist")
        void shouldReturn204NoContentWhenNoAnomalies() {
            // given
            when(anomalyFacade.getAllAnomalies()).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAllAnomalies();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAllAnomalies();
        }
    }

    @Nested
    @DisplayName("getAnomalyById() tests")
    class GetAnomalyByIdTests {

        @Test
        @DisplayName("Should return 200 OK when valid id provided")
        void shouldReturn200OkWhenValidId() {
            // given
            when(anomalyFacade.getAnomalyById(1)).thenReturn(testAnomalyDTO1);

            // when
            ResponseEntity<AnomalyDTO> response = anomalyController.getAnomalyById(1);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().getAnomalyId());
            verify(anomalyFacade, times(1)).getAnomalyById(1);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when id is invalid")
        void shouldReturn400BadRequestWhenIdInvalid() {
            // given
            when(anomalyFacade.getAnomalyById(0)).thenReturn(null);

            // when
            ResponseEntity<AnomalyDTO> response = anomalyController.getAnomalyById(0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomalyById(0);
        }

        @Test
        @DisplayName("Should return 204 No Content when anomaly not found")
        void shouldReturn204NoContentWhenAnomalyNotFound() {
            // given
            when(anomalyFacade.getAnomalyById(999)).thenReturn(null);

            // when
            ResponseEntity<AnomalyDTO> response = anomalyController.getAnomalyById(999);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomalyById(999);
        }
    }

    @Nested
    @DisplayName("getAnomaliesByType() tests")
    class GetAnomaliesByTypeTests {

        @Test
        @DisplayName("Should return 200 OK when anomalies exist for type")
        void shouldReturn200OkWhenAnomaliesExist() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO1);
            when(anomalyFacade.getAnomalyByType("BIGGER_THEN_AVG_CHECK")).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByType("BIGGER_THEN_AVG_CHECK");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            verify(anomalyFacade, times(1)).getAnomalyByType("BIGGER_THEN_AVG_CHECK");
        }

        @Test
        @DisplayName("Should return 204 No Content when no anomalies for type")
        void shouldReturn204NoContentWhenNoAnomalies() {
            // given
            when(anomalyFacade.getAnomalyByType("NON_EXISTENT")).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByType("NON_EXISTENT");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomalyByType("NON_EXISTENT");
        }
    }

    @Nested
    @DisplayName("Sum endpoints tests")
    class SumEndpointsTests {

        @Test
        @DisplayName("getAnomaliesSumFrom - Should return 200 OK")
        void shouldReturn200OkForSumRange() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO1);
            when(anomalyFacade.getAnomaliesBySumRange(1000.0, 5000.0)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesSumFrom(1000.0, 5000.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesBySumRange(1000.0, 5000.0);
        }

        @Test
        @DisplayName("getAnomaliesByMoreSum - Should return 200 OK")
        void shouldReturn200OkForMoreSum() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO1);
            when(anomalyFacade.getAnomaliesByMoreSum(3000.0)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByMoreSum(3000.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByMoreSum(3000.0);
        }

        @Test
        @DisplayName("getAnomaliesByLessSum - Should return 200 OK")
        void shouldReturn200OkForLessSum() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO2);
            when(anomalyFacade.getAnomaliesByLessSum(3000.0)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByLessSum(3000.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByLessSum(3000.0);
        }
    }

    @Nested
    @DisplayName("Event time endpoints tests")
    class EventTimeEndpointsTests {

        @Test
        @DisplayName("getAnomaliesByEventTimeRange - Should return 200 OK")
        void shouldReturn200OkForEventTimeRange() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO1);
            when(anomalyFacade.getAnomaliesByEventTimeRange(1000L, 5000L)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByEventTimeRange(1000L, 5000L);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByEventTimeRange(1000L, 5000L);
        }

        @Test
        @DisplayName("getAnomaliesByMaxEventTime - Should return 200 OK")
        void shouldReturn200OkForMaxEventTime() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO1);
            when(anomalyFacade.getAnomaliesByMaxEventTime(5000L)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByMaxEventTime(5000L);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByMaxEventTime(5000L);
        }

        @Test
        @DisplayName("getAnomaliesByMinEventTime - Should return 200 OK")
        void shouldReturn200OkForMinEventTime() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO1);
            when(anomalyFacade.getAnomaliesByMinEventTime(1000L)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByMinEventTime(1000L);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByMinEventTime(1000L);
        }
    }

    @Nested
    @DisplayName("AvgCheck endpoints tests")
    class AvgCheckEndpointsTests {

        @Test
        @DisplayName("getAnomaliesByCheck - single param should return 200 OK")
        void shouldReturn200OkForAvgCheckSingleParam() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO1);
            when(anomalyFacade.getAnomaliesByAvgCheck(1000.0)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByCheck(1000.0, null);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByAvgCheck(1000.0);
        }

        @Test
        @DisplayName("getAnomaliesByCheck - two params should return 200 OK")
        void shouldReturn200OkForAvgCheckTwoParams() {
            // given
            List<AnomalyDTO> expectedAnomalies = Collections.singletonList(testAnomalyDTO1);
            when(anomalyFacade.getAnomaliesByAvgCheck(500.0, 1000.0)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByCheck(500.0, 1000.0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByAvgCheck(500.0, 1000.0);
        }
    }

    @Nested
    @DisplayName("getAnomaliesByUser() tests")
    class GetAnomaliesByUserTests {

        @Test
        @DisplayName("Should return 200 OK when anomalies exist for user")
        void shouldReturn200OkWhenAnomaliesExist() {
            // given
            List<AnomalyDTO> expectedAnomalies = Arrays.asList(testAnomalyDTO1, testAnomalyDTO2);
            when(anomalyFacade.getAnomaliesByUserId(1)).thenReturn(expectedAnomalies);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByUser(1);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(anomalyFacade, times(1)).getAnomaliesByUserId(1);
        }

        @Test
        @DisplayName("Should return 204 No Content when no anomalies for user")
        void shouldReturn204NoContentWhenNoAnomalies() {
            // given
            when(anomalyFacade.getAnomaliesByUserId(999)).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByUser(999);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByUserId(999);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when user id is invalid")
        void shouldReturn400BadRequestWhenUserIdInvalid() {
            // given
            when(anomalyFacade.getAnomaliesByUserId(0)).thenReturn(null);

            // when
            ResponseEntity<List<AnomalyDTO>> response = anomalyController.getAnomaliesByUser(0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            verify(anomalyFacade, times(1)).getAnomaliesByUserId(0);
        }
    }
}