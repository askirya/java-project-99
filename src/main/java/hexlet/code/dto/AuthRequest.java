package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Authentication request DTO.
 */
@Getter
@Setter
public class AuthRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
