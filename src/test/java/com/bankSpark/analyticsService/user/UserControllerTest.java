package com.bankSpark.analyticsService.user;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.controller.UserController;
import com.bankSpark.analyticsService.facade.users.UserFacade;
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
@DisplayName("UserController Unit Tests")
class UserControllerTest {

    @Mock
    private UserFacade userFacade;

    @InjectMocks
    private UserController userController;

    private UserDTO testUserDTO1;
    private UserDTO testUserDTO2;

    @BeforeEach
    void setUp() {
        testUserDTO1 = new UserDTO();
        testUserDTO1.setUser_id(1);
        testUserDTO1.setFirstname("John");
        testUserDTO1.setLastname("Doe");

        testUserDTO2 = new UserDTO();
        testUserDTO2.setUser_id(2);
        testUserDTO2.setFirstname("Jane");
        testUserDTO2.setLastname("Smith");
    }

    @Nested
    @DisplayName("getAllUsers() tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return 200 OK with list of users")
        void shouldReturn200OkWithUsersList() {
            // given
            List<UserDTO> expectedUsers = Arrays.asList(testUserDTO1, testUserDTO2);
            when(userFacade.getAllUsers()).thenReturn(expectedUsers);

            // when
            ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertEquals("John", response.getBody().get(0).getFirstname());
            verify(userFacade, times(1)).getAllUsers();
        }

        @Test
        @DisplayName("Should return 204 No Content when no users exist")
        void shouldReturn204NoContentWhenNoUsers() {
            // given
            when(userFacade.getAllUsers()).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(userFacade, times(1)).getAllUsers();
        }
    }

    @Nested
    @DisplayName("getUserById() tests")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return 200 OK with user when valid id provided")
        void shouldReturn200OkWithUser() {
            // given
            when(userFacade.getUserById(1)).thenReturn(testUserDTO1);

            // when
            ResponseEntity<UserDTO> response = userController.getUserById(1);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().getUser_id());
            assertEquals("John", response.getBody().getFirstname());
            verify(userFacade, times(1)).getUserById(1);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when id is invalid")
        void shouldReturn400BadRequestWhenIdInvalid() {
            // given
            // НЕ НАДО mocking, потому что фасад не должен вызываться
            // Но если вызывается - проверим, что он вызван с неправильным id
            when(userFacade.getUserById(0)).thenReturn(null);

            // when
            ResponseEntity<UserDTO> response = userController.getUserById(0);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            // Фасад ВЫЗЫВАЕТСЯ (по вашей логике)
            verify(userFacade, times(1)).getUserById(0);
        }

        @Test
        @DisplayName("Should return 204 No Content when user not found")
        void shouldReturn204NoContentWhenUserNotFound() {
            // given
            when(userFacade.getUserById(999)).thenReturn(null);

            // when
            ResponseEntity<UserDTO> response = userController.getUserById(999);

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(userFacade, times(1)).getUserById(999);
        }
    }

    @Nested
    @DisplayName("getUserByName() tests")
    class GetUserByNameTests {

        @Test
        @DisplayName("Should return 200 OK with users matching name")
        void shouldReturn200OkWithUsers() {
            // given
            List<UserDTO> expectedUsers = Collections.singletonList(testUserDTO1);
            when(userFacade.getUsersByFirstName("John")).thenReturn(expectedUsers);

            // when
            ResponseEntity<List<UserDTO>> response = userController.getUserByName("John");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("John", response.getBody().get(0).getFirstname());
            verify(userFacade, times(1)).getUsersByFirstName("John");
        }

        @Test
        @DisplayName("Should return 400 Bad Request when name is empty")
        void shouldReturn400BadRequestWhenNameEmpty() {
            // given
            when(userFacade.getUsersByFirstName("")).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<UserDTO>> response = userController.getUserByName("");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            // Фасад ВЫЗЫВАЕТСЯ (по вашей логике)
            verify(userFacade, times(1)).getUsersByFirstName("");
        }

        @Test
        @DisplayName("Should return 204 No Content when no users match name")
        void shouldReturn204NoContentWhenNoUsersMatch() {
            // given
            when(userFacade.getUsersByFirstName("NonExistent")).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<UserDTO>> response = userController.getUserByName("NonExistent");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(userFacade, times(1)).getUsersByFirstName("NonExistent");
        }
    }

    @Nested
    @DisplayName("getUserByLastName() tests")
    class GetUserByLastNameTests {

        @Test
        @DisplayName("Should return 200 OK with users matching last name")
        void shouldReturn200OkWithUsers() {
            // given
            List<UserDTO> expectedUsers = Collections.singletonList(testUserDTO1);
            when(userFacade.getUsersByLastName("Doe")).thenReturn(expectedUsers);

            // when
            ResponseEntity<List<UserDTO>> response = userController.getUserByLastName("Doe");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("Doe", response.getBody().get(0).getLastname());
            verify(userFacade, times(1)).getUsersByLastName("Doe");
        }

        @Test
        @DisplayName("Should return 400 Bad Request when last name is empty")
        void shouldReturn400BadRequestWhenLastNameEmpty() {
            // given
            when(userFacade.getUsersByLastName("")).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<UserDTO>> response = userController.getUserByLastName("");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            // Фасад ВЫЗЫВАЕТСЯ (по вашей логике)
            verify(userFacade, times(1)).getUsersByLastName("");
        }

        @Test
        @DisplayName("Should return 204 No Content when no users match last name")
        void shouldReturn204NoContentWhenNoUsersMatch() {
            // given
            when(userFacade.getUsersByLastName("NonExistent")).thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<UserDTO>> response = userController.getUserByLastName("NonExistent");

            // then
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(userFacade, times(1)).getUsersByLastName("NonExistent");
        }
    }
}