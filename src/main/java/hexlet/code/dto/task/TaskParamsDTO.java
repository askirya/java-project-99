package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

/**
 * Query parameters for task filtering.
 */
@Getter
@Setter
public class TaskParamsDTO {
    private String titleCont;
    private Long assigneeId;
    private String status;
    private Long labelId;
}
