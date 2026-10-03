package in.sj.service;

import in.sj.dto.LoginRequest;
import in.sj.dto.LoginResponse;
import in.sj.dto.UserRequest;

public interface UserService {

    LoginResponse register(UserRequest request);

    LoginResponse login(LoginRequest request);
}