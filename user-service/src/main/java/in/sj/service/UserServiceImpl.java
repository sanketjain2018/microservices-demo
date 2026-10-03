package in.sj.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.sj.dto.LoginRequest;
import in.sj.dto.LoginResponse;
import in.sj.dto.UserRequest;
import in.sj.entity.User;
import in.sj.exception.InvalidCredentialsException;
import in.sj.repository.UserRepository;
import in.sj.security.JwtService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse register(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole("USER");

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );

        return new LoginResponse(
                token,
                "Bearer",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid username or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Invalid username or password"
            );
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}