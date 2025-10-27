package group25.sep.server.controller;

import group25.sep.server.model.LoginRequest;
import group25.sep.server.service.LoginService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/login")

public class LoginController {
    private final LoginService loginService;

    public LoginController (LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping
    public String login(@RequestBody LoginRequest loginRequest) {
            String role = loginService.login(loginRequest);
            return role;
    }
}
