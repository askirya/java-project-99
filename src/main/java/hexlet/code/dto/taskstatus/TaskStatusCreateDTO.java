package hexlet.code.dto.taskstatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a task status.
 */
@Getter
@Setter
public class TaskStatusCreateDTO {

    @NotBlank
    @Size(min = 1)
    private String name;

    @NotBlank
    @Size(min = 1)
    private String slug;
}
