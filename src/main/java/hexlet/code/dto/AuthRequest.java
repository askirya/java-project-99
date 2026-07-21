package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Authentication request DTO.
 */
@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;
}
