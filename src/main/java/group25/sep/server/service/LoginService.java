package group25.sep.server.service;

import group25.sep.server.model.LoginRequest;
import group25.sep.server.model.LoginResponse;
import group25.sep.server.model.User;

import java.util.List;

public interface LoginService {
   LoginResponse login(LoginRequest loginRequest);

   List<User> getAllUsers();

   boolean addUser(User newUser);

   boolean updateUserPassword(String username, String newPassword);
}
