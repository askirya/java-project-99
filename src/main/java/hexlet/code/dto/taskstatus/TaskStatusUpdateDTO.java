package hexlet.code.dto.taskstatus;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * DTO for partial task status update.
 */
@Getter
@Setter
public class TaskStatusUpdateDTO {

    @Size(min = 1)
    private JsonNullable<String> name;

    @Size(min = 1)
    private JsonNullable<String> slug;
}
