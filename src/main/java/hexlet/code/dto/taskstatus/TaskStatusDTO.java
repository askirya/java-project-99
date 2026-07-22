package hexlet.code.dto.taskstatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Task status response DTO.
 */
@Getter
@Setter
public class TaskStatusDTO {
    private Long id;
    private String name;
    private String slug;
    private LocalDate createdAt;
}
