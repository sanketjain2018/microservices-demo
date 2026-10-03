package in.sj.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String username;
    private String role;

    public LoginResponse(
            String token,
            String tokenType,
            Long userId,
            String username,
            String role) {

        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}