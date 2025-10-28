package group25.sep.server.controller;

import group25.sep.server.model.LoginRequest;
import group25.sep.server.model.LoginResponse;
import group25.sep.server.model.User;
import group25.sep.server.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = loginService.login(loginRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = loginService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        boolean success = loginService.addUser(newUser);
        if (success) {
            return ResponseEntity.ok("User added successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to add user. Username might already exist.");
        }
    }

    @PutMapping("/users/{username}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable String username,
            @RequestBody String newPassword) {
        boolean success = loginService.updateUserPassword(username, newPassword);
        if (success) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update password. User not found.");
        }
    }
}
