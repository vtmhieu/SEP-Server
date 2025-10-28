package group25.sep.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import group25.sep.server.model.LoginRequest;
import group25.sep.server.model.LoginResponse;
import group25.sep.server.model.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {

    // Simple in-memory user database
    // In a real application, this would be stored in a database
    private final Map<String, User> users = initializeUsers();

    /**
     * Initialize users with their credentials
     * This is a simple approach - in production, use a database
     */
    private Map<String, User> initializeUsers() {
        Map<String, User> userMap = new HashMap<>();

        // Management roles
        userMap.put("CSO", User.builder()
                .username("CSO")
                .password("cso123")
                .role("CSO")
                .department("MANAGEMENT")
                .build());

        userMap.put("SCSO", User.builder()
                .username("SCSO")
                .password("scso123")
                .role("SCSO")
                .department("MANAGEMENT")
                .build());

        // Department managers
        userMap.put("AM", User.builder()
                .username("AM")
                .password("am123")
                .role("AM")
                .department("MANAGEMENT")
                .build());

        userMap.put("FM", User.builder()
                .username("FM")
                .password("fm123")
                .role("FM")
                .department("FINANCE")
                .build());

        userMap.put("PM", User.builder()
                .username("PM")
                .password("pm123")
                .role("PM")
                .department("PRODUCTION")
                .build());

        userMap.put("SM", User.builder()
                .username("SM")
                .password("sm123")
                .role("SM")
                .department("SERVICES")
                .build());

        userMap.put("HR", User.builder()
                .username("HR")
                .password("hr123")
                .role("HR")
                .department("HUMAN_RESOURCES")
                .build());

        // Service providers
        userMap.put("Food", User.builder()
                .username("Food")
                .password("food123")
                .role("FOOD_PROVIDER")
                .department("SERVICES")
                .build());

        userMap.put("Music", User.builder()
                .username("Music")
                .password("music123")
                .role("MUSIC_PROVIDER")
                .department("SERVICES")
                .build());

        return userMap;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUser();
        String password = loginRequest.getPass();

        // Validate input
        if (username == null || username.trim().isEmpty()) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Username is required")
                    .build();
        }

        if (password == null || password.trim().isEmpty()) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Password is required")
                    .build();
        }

        // Check if user exists
        User user = users.get(username);
        if (user == null) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Invalid username or password")
                    .build();
        }

        // Check password
        if (!user.getPassword().equals(password)) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Invalid username or password")
                    .build();
        }

        // Successful login
        return LoginResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .department(user.getDepartment())
                .message("Login successful")
                .success(true)
                .build();
    }

    /**
     * Get all available users (for admin purposes)
     */
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    /**
     * Add a new user (for admin purposes)
     */
    public boolean addUser(User newUser) {
        if (newUser.getUsername() == null || users.containsKey(newUser.getUsername())) {
            return false;
        }
        users.put(newUser.getUsername(), newUser);
        return true;
    }

    /**
     * Update user password (for admin purposes)
     */
    public boolean updateUserPassword(String username, String newPassword) {
        User user = users.get(username);
        if (user == null) {
            return false;
        }
        user.setPassword(newPassword);
        return true;
    }
}
