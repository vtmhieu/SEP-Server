package group25.sep.server.service;

import group25.sep.server.model.LoginRequest;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class LoginServiceImplTest {

    private LoginService loginService = new LoginServiceImpl();
    private final List<String> validUsernames = Arrays.asList("CSO", "SCSO", "AM", "FM", "PM", "SM", "HR", "Food", "Music");



    @Test
    void LoginWithValidCredentials_ShouldReturnCorrectRoleEachTime() {
        for (int i = 0; i < validUsernames.size(); i++){
            //ARRANGE
            String username = validUsernames.get(i);
            LoginRequest loginRequest = LoginRequest.builder()
                                .user(username)
                                .pass("Password")
                                .build();
            //ACT 
            String result = loginService.login(loginRequest);

            //ASSERT
            assertThat(result).isEqualTo(username);
        }
    }

    @Test
    void LoginWithInvalidUsername_ShouldThrowException() {
        //ARRANGE
        String username = "no_role";
        LoginRequest loginRequest = LoginRequest.builder()
                            .user(username)
                            .pass("Password")
                            .build();

        //ACT/ASSERT
        assertThrows(RuntimeException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void LoginWithInvalidPassword_ShouldThrowException() {
        //ARRANGE
        String username = "AM";
        LoginRequest loginRequest = LoginRequest.builder()
                            .user(username)
                            .pass("invalid")
                            .build();

        //ACT/ASSERT
        assertThrows(RuntimeException.class, () -> loginService.login(loginRequest));
    }
}

