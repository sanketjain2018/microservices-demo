package in.sj.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import in.sj.dto.LoginRequest;
import in.sj.dto.LoginResponse;
import in.sj.dto.UserRequest;
import in.sj.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @Valid @RequestBody UserRequest request) {

        LoginResponse response =
                userService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                userService.login(request);

        return ResponseEntity.ok(response);
    }
}