package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

/**
 * DTO for partial task update.
 */
@Getter
@Setter
public class TaskUpdateDTO {

    private JsonNullable<Integer> index = JsonNullable.undefined();

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId = JsonNullable.undefined();

    @Size(min = 1)
    private JsonNullable<String> title = JsonNullable.undefined();

    private JsonNullable<String> content = JsonNullable.undefined();

    private JsonNullable<String> status = JsonNullable.undefined();

    private JsonNullable<Set<Long>> taskLabelIds = JsonNullable.undefined();
}
