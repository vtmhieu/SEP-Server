package group25.sep.server.service;

import group25.sep.server.model.LoginRequest;
import group25.sep.server.model.LoginResponse;
import group25.sep.server.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoginServiceImplTest {

    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        loginService = new LoginServiceImpl();
    }

    @Test
    void login_WithValidCredentials_ShouldReturnSuccessResponse() {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .user("PM")
                .pass("pm123")
                .build();

        // When
        LoginResponse response = loginService.login(loginRequest);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getUsername()).isEqualTo("PM");
        assertThat(response.getRole()).isEqualTo("PM");
        assertThat(response.getDepartment()).isEqualTo("PRODUCTION");
        assertThat(response.getMessage()).isEqualTo("Login successful");
    }

    @Test
    void login_WithInvalidUsername_ShouldReturnFailureResponse() {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .user("INVALID_USER")
                .pass("pm123")
                .build();

        // When
        LoginResponse response = loginService.login(loginRequest);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Invalid username or password");
    }

    @Test
    void login_WithInvalidPassword_ShouldReturnFailureResponse() {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .user("PM")
                .pass("wrong_password")
                .build();

        // When
        LoginResponse response = loginService.login(loginRequest);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Invalid username or password");
    }

    @Test
    void login_WithEmptyUsername_ShouldReturnFailureResponse() {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .user("")
                .pass("pm123")
                .build();

        // When
        LoginResponse response = loginService.login(loginRequest);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Username is required");
    }

    @Test
    void login_WithEmptyPassword_ShouldReturnFailureResponse() {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .user("PM")
                .pass("")
                .build();

        // When
        LoginResponse response = loginService.login(loginRequest);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Password is required");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // When
        List<User> users = loginService.getAllUsers();

        // Then
        assertThat(users).hasSize(9); // We have 9 predefined users
        assertThat(users).extracting(User::getUsername)
                .contains("CSO", "SCSO", "AM", "FM", "PM", "SM", "HR", "Food", "Music");
    }

    @Test
    void addUser_WithValidUser_ShouldReturnTrue() {
        // Given
        User newUser = User.builder()
                .username("TEST_USER")
                .password("test123")
                .role("TEST_ROLE")
                .department("TEST_DEPT")
                .build();

        // When
        boolean result = loginService.addUser(newUser);

        // Then
        assertThat(result).isTrue();

        // Verify user was added
        LoginRequest loginRequest = LoginRequest.builder()
                .user("TEST_USER")
                .pass("test123")
                .build();
        LoginResponse response = loginService.login(loginRequest);
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    void addUser_WithExistingUsername_ShouldReturnFalse() {
        // Given
        User existingUser = User.builder()
                .username("PM") // This username already exists
                .password("newpass123")
                .role("PM")
                .department("PRODUCTION")
                .build();

        // When
        boolean result = loginService.addUser(existingUser);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void updateUserPassword_WithValidUser_ShouldReturnTrue() {
        // Given
        String username = "PM";
        String newPassword = "newpm123";

        // When
        boolean result = loginService.updateUserPassword(username, newPassword);

        // Then
        assertThat(result).isTrue();

        // Verify password was updated
        LoginRequest loginRequest = LoginRequest.builder()
                .user(username)
                .pass(newPassword)
                .build();
        LoginResponse response = loginService.login(loginRequest);
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    void updateUserPassword_WithInvalidUser_ShouldReturnFalse() {
        // Given
        String username = "NON_EXISTENT_USER";
        String newPassword = "newpass123";

        // When
        boolean result = loginService.updateUserPassword(username, newPassword);

        // Then
        assertThat(result).isFalse();
    }
}