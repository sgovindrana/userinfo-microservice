package com.project.userinfo.service;

import com.project.userinfo.dto.UserDTO;
import com.project.userinfo.entity.User;
import com.project.userinfo.mapper.UserMapper;
import com.project.userinfo.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        // Arrange
        UserDTO inputDto = new UserDTO(1, "bob", "secret", "1 Road St", "Springfield");
        User savedEntity = new User(1, "bob", "secret", "1 Road St", "Springfield");

        // Use any() matcher so stub matches the mapper-created User
        when(userRepo.save(any(User.class))).thenReturn(savedEntity);

        // Act
        UserDTO result = userService.addUser(inputDto);

        // Assert
        assertNotNull(result, "Service should never return null");      // now passes
        assertEquals(savedEntity.getUserId(),      result.getUserId());
        assertEquals(savedEntity.getUserName(), result.getUserName());
        assertEquals(savedEntity.getAddress(),  result.getAddress());
        verify(userRepo, times(1)).save(any(User.class));
    }


    @Test
    void fetchUserDetailsById_WhenFound_ShouldReturnOk() {
        // Arrange
        Integer id = 42;
        User found = new User(id, "alice", "pw", "123 Ave", "Metropolis");
        when(userRepo.findById(id)).thenReturn(Optional.of(found));

        // Act
        ResponseEntity<UserDTO> response = userService.fetchUserDetailsById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO dto = response.getBody();
        assertNotNull(dto);
        assertEquals(found.getUserId(), dto.getUserId());
        assertEquals(found.getUserName(), dto.getUserName());
        verify(userRepo, times(1)).findById(id);
    }

    @Test
    void fetchUserDetailsById_WhenNotFound_ShouldReturnNotFound() {
        // Arrange
        Integer id = 99;
        when(userRepo.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserDTO> response = userService.fetchUserDetailsById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userRepo, times(1)).findById(id);
    }
}

