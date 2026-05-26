package com.bankSpark.analyticsService.analyst;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.AuthAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.controller.AnalystController;
import com.bankSpark.analyticsService.facade.analyst.AnalystFacade;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtTokenDTO;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnalystController Unit Tests")
class AnalystControllerTest {

    @Mock
    private AnalystFacade analystFacade;

    @InjectMocks
    private AnalystController analystController;

    private AnalystInfoDTO testAnalystDTO1;
    private AnalystInfoDTO testAnalystDTO2;
    private CreateAnalystDTO createDTO;
    private UpdateAnalystDTO updateDTO;
    private AuthAnalystDTO authDTO;
    private JwtAuthenticationDTO jwtAuthDTO;
    private JwtTokenDTO jwtTokenDTO;

    @BeforeEach
    void setUp() {
        testAnalystDTO1 = new AnalystInfoDTO();
        testAnalystDTO1.setLogin("john_analyst");
        testAnalystDTO1.setRole("ANALYST");
        testAnalystDTO1.setToken("ANALYST-ABC123");

        testAnalystDTO2 = new AnalystInfoDTO();
        testAnalystDTO2.setLogin("jane_analyst");
        testAnalystDTO2.setRole("ANALYST");
        testAnalystDTO2.setToken("ANALYST-DEF456");

        createDTO = new CreateAnalystDTO();
        createDTO.setToken("ANALYST-ABC123");
        createDTO.setLogin("john_analyst");
        createDTO.setPassword("password123");

        updateDTO = new UpdateAnalystDTO();
        updateDTO.setLogin("john_updated");

        authDTO = new AuthAnalystDTO();
        authDTO.setLogin("john_analyst");
        authDTO.setPassword("password123");
        authDTO.setToken("ANALYST-ABC123");

        jwtAuthDTO = new JwtAuthenticationDTO();
        jwtAuthDTO.setToken("eyJhbGciOiJIUzI1NiJ9...");
        jwtAuthDTO.setRefreshToken("eyJhbGciOiJIUzI1NiJ9...");

        jwtTokenDTO = new JwtTokenDTO();
        jwtTokenDTO.setToken("eyJhbGciOiJIUzI1NiJ9...");
    }

    @Nested
    @DisplayName("getAnalysts() tests")
    class GetAnalystsTests {

        @Test
        @DisplayName("Should return 200 OK with list of analysts")
        void shouldReturn200OkWithAnalystsList() {
            // given
            List<AnalystInfoDTO> expectedAnalysts = Arrays.asList(testAnalystDTO1, testAnalystDTO2);
            when(analystFacade.getAnalysts()).thenReturn(expectedAnalysts);

            // when
            ResponseEntity<List<AnalystInfoDTO>> response = analystController.getAnalysts();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(analystFacade, times(1)).getAnalysts();
        }

        @Test
        @DisplayName("Should return 204 No Content when no analysts exist")
        void shouldReturn204NoContentWhenNoAnalysts() {
            // given
            when(analystFacade.getAnalysts()).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<AnalystInfoDTO>> response = analystController.getAnalysts();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(analystFacade, times(1)).getAnalysts();
        }
    }

    @Nested
    @DisplayName("getAnalystsByRole() tests")
    class GetAnalystsByRoleTests {

        @Test
        @DisplayName("Should return 200 OK when analysts exist for role")
        void shouldReturn200OkWhenAnalystsExist() {
            // given
            List<AnalystInfoDTO> expectedAnalysts = Collections.singletonList(testAnalystDTO1);
            when(analystFacade.getAnalystsByRole("ANALYST")).thenReturn(expectedAnalysts);

            // when
            ResponseEntity<List<AnalystInfoDTO>> response = analystController.getAnalystsByRole("ANALYST");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            verify(analystFacade, times(1)).getAnalystsByRole("ANALYST");
        }

        @Test
        @DisplayName("Should return 204 No Content when no analysts for role")
        void shouldReturn204NoContentWhenNoAnalysts() {
            // given
            when(analystFacade.getAnalystsByRole("ADMIN")).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<AnalystInfoDTO>> response = analystController.getAnalystsByRole("ADMIN");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(analystFacade, times(1)).getAnalystsByRole("ADMIN");
        }
    }

    @Nested
    @DisplayName("getAnalystById() tests")
    class GetAnalystByIdTests {

        @Test
        @DisplayName("Should return 200 OK when valid id provided")
        void shouldReturn200OkWhenValidId() {
            // given
            when(analystFacade.getAnalystById(1)).thenReturn(testAnalystDTO1);

            // when
            ResponseEntity<AnalystInfoDTO> response = analystController.getAnalystById(1);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("john_analyst", response.getBody().getLogin());
            verify(analystFacade, times(1)).getAnalystById(1);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when id is invalid")
        void shouldReturn400BadRequestWhenIdInvalid() {
            // given
            when(analystFacade.getAnalystById(0)).thenReturn(null);

            // when
            ResponseEntity<AnalystInfoDTO> response = analystController.getAnalystById(0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            verify(analystFacade, times(1)).getAnalystById(0);
        }

        @Test
        @DisplayName("Should return 204 No Content when analyst not found")
        void shouldReturn204NoContentWhenAnalystNotFound() {
            // given
            when(analystFacade.getAnalystById(999)).thenReturn(null);

            // when
            ResponseEntity<AnalystInfoDTO> response = analystController.getAnalystById(999);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(analystFacade, times(1)).getAnalystById(999);
        }
    }

    @Nested
    @DisplayName("getAnalystByLogin() tests")
    class GetAnalystByLoginTests {

        @Test
        @DisplayName("Should return 200 OK when valid login provided")
        void shouldReturn200OkWhenValidLogin() {
            // given
            when(analystFacade.getAnalystByLogin("john_analyst")).thenReturn(testAnalystDTO1);

            // when
            ResponseEntity<AnalystInfoDTO> response = analystController.getAnalystByLogin("john_analyst");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(analystFacade, times(1)).getAnalystByLogin("john_analyst");
        }

        @Test
        @DisplayName("Should return 400 Bad Request when login is empty")
        void shouldReturn400BadRequestWhenLoginEmpty() {
            // given
            when(analystFacade.getAnalystByLogin("")).thenReturn(null);

            // when
            ResponseEntity<AnalystInfoDTO> response = analystController.getAnalystByLogin("");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            verify(analystFacade, times(1)).getAnalystByLogin("");
        }
    }

    @Nested
    @DisplayName("addAnalyst() tests")
    class AddAnalystTests {

        @Test
        @DisplayName("Should return 200 OK when analyst created successfully")
        void shouldReturn200OkWhenAnalystCreated() {
            // given
            when(analystFacade.createAnalyst(createDTO)).thenReturn(testAnalystDTO1);

            // when
            ResponseEntity<AnalystInfoDTO> response = analystController.addAnalyst(createDTO);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(analystFacade, times(1)).createAnalyst(createDTO);
        }
    }

    @Nested
    @DisplayName("updateAnalyst() tests")
    class UpdateAnalystTests {

        @Test
        @DisplayName("Should return 200 OK when analyst updated successfully")
        void shouldReturn200OkWhenAnalystUpdated() {
            // given
            when(analystFacade.updateAnalyst(1, updateDTO)).thenReturn(testAnalystDTO1);

            // when
            ResponseEntity<AnalystInfoDTO> response = analystController.patchAnalystById(1, updateDTO);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(analystFacade, times(1)).updateAnalyst(1, updateDTO);
        }
    }

    @Nested
    @DisplayName("deleteAnalystById() tests")
    class DeleteAnalystByIdTests {

        @Test
        @DisplayName("Should return 200 OK when analyst deleted successfully")
        void shouldReturn200OkWhenAnalystDeleted() {
            // given
            doNothing().when(analystFacade).deleteAnalyst(1);

            // when
            ResponseEntity<?> response = analystController.deleteAnalystById(1);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(analystFacade, times(1)).deleteAnalyst(1);
        }
    }

    @Nested
    @DisplayName("signIn() tests")
    class SignInTests {

        @Test
        @DisplayName("Should return 200 OK when authentication successful")
        void shouldReturn200OkWhenAuthenticationSuccessful() {
            // given
            when(analystFacade.signIn(authDTO)).thenReturn(jwtAuthDTO);

            // when
            ResponseEntity<JwtAuthenticationDTO> response = analystController.signIn(authDTO);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(analystFacade, times(1)).signIn(authDTO);
        }
    }

    @Nested
    @DisplayName("exit() tests")
    class ExitTests {

        @Test
        @DisplayName("Should return 200 OK when logout successful")
        void shouldReturn200OkWhenLogoutSuccessful() {
            // given
            String fullToken = "Bearer eyJhbGciOiJIUzI1NiJ9...";
            // ✅ Исправлено: мокаем с полным токеном (включая "Bearer ")
            when(analystFacade.getOut(fullToken)).thenReturn(jwtTokenDTO);

            // when
            ResponseEntity<JwtTokenDTO> response = analystController.exit(fullToken);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(analystFacade, times(1)).getOut(fullToken);
        }
    }

    @Nested
    @DisplayName("getAnalystAfterDate() tests")
    class GetAnalystAfterDateTests {

        @Test
        @DisplayName("Should return 200 OK with analysts after date")
        void shouldReturn200OkWithAnalystsAfterDate() {
            // given
            LocalDateTime afterDate = LocalDateTime.now().minusDays(1);
            List<AnalystInfoDTO> expectedAnalysts = Collections.singletonList(testAnalystDTO1);
            when(analystFacade.getAnalystsAfterCreatedAt(afterDate)).thenReturn(expectedAnalysts);

            // when
            ResponseEntity<List<AnalystInfoDTO>> response = analystController.getAnalystAfterDate(afterDate);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            verify(analystFacade, times(1)).getAnalystsAfterCreatedAt(afterDate);
        }

        @Test
        @DisplayName("Should return 204 No Content when no analysts after date")
        void shouldReturn204NoContentWhenNoAnalysts() {
            // given
            LocalDateTime afterDate = LocalDateTime.now().minusDays(1);
            when(analystFacade.getAnalystsAfterCreatedAt(afterDate)).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<AnalystInfoDTO>> response = analystController.getAnalystAfterDate(afterDate);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(analystFacade, times(1)).getAnalystsAfterCreatedAt(afterDate);
        }
    }

    @Nested
    @DisplayName("getAnalystBeforeDate() tests")
    class GetAnalystBeforeDateTests {

        @Test
        @DisplayName("Should return 200 OK with analysts before date")
        void shouldReturn200OkWithAnalystsBeforeDate() {
            // given
            LocalDateTime beforeDate = LocalDateTime.now().plusDays(1);
            List<AnalystInfoDTO> expectedAnalysts = Collections.singletonList(testAnalystDTO1);
            when(analystFacade.getAnalystsBeforeCreatedAt(beforeDate)).thenReturn(expectedAnalysts);

            // when
            ResponseEntity<List<AnalystInfoDTO>> response = analystController.getAnalystBeforeDate(beforeDate);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            verify(analystFacade, times(1)).getAnalystsBeforeCreatedAt(beforeDate);
        }
    }

    @Nested
    @DisplayName("getFromJWT() tests")
    class GetFromJWTTests {

        @Test
        @DisplayName("Should return 200 OK when analyst extracted from token")
        void shouldReturn200OkWhenAnalystExtracted() {
            // given
            String fullToken = "Bearer eyJhbGciOiJIUzI1NiJ9...";
            // ✅ Исправлено: мокаем с полным токеном (включая "Bearer ")
            when(analystFacade.analystFromToken(fullToken)).thenReturn(testAnalystDTO1);

            // when
            ResponseEntity<AnalystInfoDTO> response = analystController.getFromJWT(fullToken);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(analystFacade, times(1)).analystFromToken(fullToken);
        }
    }

}