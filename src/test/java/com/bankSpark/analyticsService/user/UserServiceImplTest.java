package com.bankSpark.analyticsService.user;

import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.repository.UserRepository;
import com.bankSpark.analyticsService.service.users.UserServiceImpl;
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
@DisplayName("UserServiceImpl Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser1;
    private User testUser2;

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
    }

    @Nested
    @DisplayName("getAllUsers() tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return all users when repository has data")
        void shouldReturnAllUsers() {
            // given
            List<User> expectedUsers = Arrays.asList(testUser1, testUser2);
            when(userRepository.findAll()).thenReturn(expectedUsers);

            // when
            List<User> result = userService.getAllUsers();

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("John", result.get(0).getFirstname());
            assertEquals("Jane", result.get(1).getFirstname());
            verify(userRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no users in repository")
        void shouldReturnEmptyListWhenNoUsers() {
            // given
            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            // when
            List<User> result = userService.getAllUsers();

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(userRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getUserById() tests")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return user when id exists")
        void shouldReturnUserWhenIdExists() {
            // given
            when(userRepository.findById(1)).thenReturn(Optional.of(testUser1));

            // when
            User result = userService.getUserById(1);

            // then
            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("John", result.getFirstname());
            verify(userRepository, times(1)).findById(1);
        }

        @Test
        @DisplayName("Should throw exception when id does not exist")
        void shouldThrowExceptionWhenIdDoesNotExist() {
            // given
            when(userRepository.findById(999)).thenReturn(Optional.empty());

            // when & then
            assertThrows(Exception.class, () -> userService.getUserById(999));
            verify(userRepository, times(1)).findById(999);
        }
    }

    @Nested
    @DisplayName("getUsersByLastName() tests")
    class GetUsersByLastNameTests {

        @Test
        @DisplayName("Should return users matching last name")
        void shouldReturnUsersMatchingLastName() {
            // given
            List<User> expectedUsers = Collections.singletonList(testUser1);
            when(userRepository.findByLastname("Doe")).thenReturn(expectedUsers);

            // when
            List<User> result = userService.getUsersByLastName("Doe");

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Doe", result.get(0).getLastname());
            verify(userRepository, times(1)).findByLastname("Doe");
        }

        @Test
        @DisplayName("Should return empty list when last name not found")
        void shouldReturnEmptyListWhenLastNameNotFound() {
            // given
            when(userRepository.findByLastname("NonExistent")).thenReturn(Collections.emptyList());

            // when
            List<User> result = userService.getUsersByLastName("NonExistent");

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(userRepository, times(1)).findByLastname("NonExistent");
        }
    }

    @Nested
    @DisplayName("getUsersByFirstName() tests")
    class GetUsersByFirstNameTests {

        @Test
        @DisplayName("Should return users matching first name")
        void shouldReturnUsersMatchingFirstName() {
            // given
            List<User> expectedUsers = Collections.singletonList(testUser1);
            when(userRepository.findByFirstname("John")).thenReturn(expectedUsers);

            // when
            List<User> result = userService.getUsersByFirstName("John");

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("John", result.get(0).getFirstname());
            verify(userRepository, times(1)).findByFirstname("John");
        }

        @Test
        @DisplayName("Should return empty list when first name not found")
        void shouldReturnEmptyListWhenFirstNameNotFound() {
            // given
            when(userRepository.findByFirstname("NonExistent")).thenReturn(Collections.emptyList());

            // when
            List<User> result = userService.getUsersByFirstName("NonExistent");

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(userRepository, times(1)).findByFirstname("NonExistent");
        }
    }
}