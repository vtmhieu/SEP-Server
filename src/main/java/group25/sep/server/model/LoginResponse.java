package group25.sep.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login response model
 * Returns user information after successful login
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String role;
    private String department;
    private String message;
    private boolean success;
}
