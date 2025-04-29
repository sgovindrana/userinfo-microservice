package com.project.userinfo.controller;

import com.project.userinfo.dto.UserDTO;
import com.project.userinfo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser_ShouldReturnCreatedUser() {
        // Arrange
        UserDTO input = new UserDTO(1, "john", "pass123", "123 Main St", "Metropolis");
        when(userService.addUser(input)).thenReturn(input);

        // Act
        ResponseEntity<UserDTO> response = userController.addUser(input);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(input, response.getBody());
        verify(userService, times(1)).addUser(input);
    }

    @Test
    void testFetchUserDetailsById_WhenFound_ShouldReturnOk() {
        // Arrange
        int userId = 42;
        UserDTO dto = new UserDTO(userId, "alice", "pwd456", "456 Oak Rd", "Gotham");
        ResponseEntity<UserDTO> okResponse = new ResponseEntity<>(dto, HttpStatus.OK);
        when(userService.fetchUserDetailsById(userId)).thenReturn(okResponse);

        // Act
        ResponseEntity<UserDTO> response = userController.fetchUserDetailsById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(userService, times(1)).fetchUserDetailsById(userId);
    }

    @Test
    void testFetchUserDetailsById_WhenNotFound_ShouldReturnNotFound() {
        // Arrange
        int missingId = 99;
        ResponseEntity<UserDTO> notFound = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        when(userService.fetchUserDetailsById(missingId)).thenReturn(notFound);

        // Act
        ResponseEntity<UserDTO> response = userController.fetchUserDetailsById(missingId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).fetchUserDetailsById(missingId);
    }
}
