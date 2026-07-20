package hexlet.code.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * User response DTO without private fields.
 */
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate createdAt;
}
