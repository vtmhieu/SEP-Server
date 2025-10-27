package group25.sep.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import group25.sep.server.model.LoginRequest;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {
    
    private final List<String> validUsernames = Arrays.asList("CSO", "SCSO", "AM", "FM", "PM", "SM", "HR", "Food", "Music");
    private final String validPassword = "Password";
    
    @Override
    public String login(LoginRequest loginRequest){
        String user = loginRequest.getUser();
        String pass = loginRequest.getPass();
        if (pass.equals(validPassword) && validUsernames.contains(user)){
            return user;
        } else {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }

}

