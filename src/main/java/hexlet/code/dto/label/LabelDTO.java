package hexlet.code.dto.label;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Label response DTO.
 */
@Getter
@Setter
public class LabelDTO {
    private Long id;
    private String name;
    private LocalDate createdAt;
}
