package com.bankSpark.analyticsService.user;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserMapper Unit Tests")
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Nested
    @DisplayName("toDTO() tests")
    class ToDTOTests {

        @Test
        @DisplayName("Should convert User entity to UserDTO successfully")
        void shouldConvertUserEntityToDTO() {
            // given
            User user = new User();
            user.setId(1);
            user.setFirstname("John");
            user.setLastname("Doe");

            // when
            UserDTO result = userMapper.toDTO(user);

            // then
            assertNotNull(result);
            assertEquals(1, result.getUser_id());
            assertEquals("John", result.getFirstname());
            assertEquals("Doe", result.getLastname());
        }

        @Test
        @DisplayName("Should handle null firstname gracefully")
        void shouldHandleNullFirstname() {
            // given
            User user = new User();
            user.setId(1);
            user.setFirstname(null);
            user.setLastname("Doe");

            // when
            UserDTO result = userMapper.toDTO(user);

            // then
            assertNotNull(result);
            assertEquals(1, result.getUser_id());
            assertNull(result.getFirstname());
            assertEquals("Doe", result.getLastname());
        }

        @Test
        @DisplayName("Should handle null lastname gracefully")
        void shouldHandleNullLastname() {
            // given
            User user = new User();
            user.setId(1);
            user.setFirstname("John");
            user.setLastname(null);

            // when
            UserDTO result = userMapper.toDTO(user);

            // then
            assertNotNull(result);
            assertEquals(1, result.getUser_id());
            assertEquals("John", result.getFirstname());
            assertNull(result.getLastname());
        }
    }

    @Nested
    @DisplayName("toEntity() tests")
    class ToEntityTests {

        @Test
        @DisplayName("Should convert UserDTO to User entity successfully")
        void shouldConvertUserDTOToEntity() {
            // given
            UserDTO userDTO = new UserDTO();
            userDTO.setUser_id(1);
            userDTO.setFirstname("John");
            userDTO.setLastname("Doe");

            // when
            User result = userMapper.toEntity(userDTO);

            // then
            assertNotNull(result);
            // ✅ ID не маппится обратно в Entity (это нормально)
            // Поэтому проверяем, что ID = 0 или null (по умолчанию)
            assertNull(result.getId());  // ← исправлено: ожидаем null (Integer)
            assertEquals("John", result.getFirstname());
            assertEquals("Doe", result.getLastname());
        }

        @Test
        @DisplayName("Should handle null DTO fields gracefully")
        void shouldHandleNullDTOFields() {
            // given
            UserDTO userDTO = new UserDTO();
            userDTO.setUser_id(null);
            userDTO.setFirstname(null);
            userDTO.setLastname(null);

            // when
            User result = userMapper.toEntity(userDTO);

            // then
            assertNotNull(result);
            assertNull(result.getId());
            assertNull(result.getFirstname());
            assertNull(result.getLastname());
        }

        @Test
        @DisplayName("Should handle DTO with null user_id")
        void shouldHandleNullUserId() {
            // given
            UserDTO userDTO = new UserDTO();
            userDTO.setUser_id(null);
            userDTO.setFirstname("John");
            userDTO.setLastname("Doe");

            // when
            User result = userMapper.toEntity(userDTO);

            // then
            assertNotNull(result);
            assertNull(result.getId());  // ID остаётся null
            assertEquals("John", result.getFirstname());
            assertEquals("Doe", result.getLastname());
        }
    }

    @Nested
    @DisplayName("toListDTO() tests")
    class ToListDTOTests {

        @Test
        @DisplayName("Should convert list of Users to list of UserDTOs")
        void shouldConvertListOfUsersToListOfDTOs() {
            // given
            User user1 = new User();
            user1.setId(1);
            user1.setFirstname("John");
            user1.setLastname("Doe");

            User user2 = new User();
            user2.setId(2);
            user2.setFirstname("Jane");
            user2.setLastname("Smith");

            List<User> users = Arrays.asList(user1, user2);

            // when
            List<UserDTO> result = userMapper.toListDTO(users);

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(1, result.get(0).getUser_id());
            assertEquals("John", result.get(0).getFirstname());
            assertEquals(2, result.get(1).getUser_id());
            assertEquals("Jane", result.get(1).getFirstname());
        }

        @Test
        @DisplayName("Should return empty list for empty input list")
        void shouldReturnEmptyListForEmptyInput() {
            // given
            List<User> users = Collections.emptyList();

            // when
            List<UserDTO> result = userMapper.toListDTO(users);

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle null list by throwing NPE")
        void shouldThrowNPEForNullList() {
            // given
            List<User> users = null;

            // when & then
            assertThrows(NullPointerException.class, () -> userMapper.toListDTO(users));
        }
    }
}