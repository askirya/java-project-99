package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;

import java.util.List;

/**
 * Task business operations.
 */
public interface TaskService {

    /**
     * Returns tasks filtered by query parameters.
     * @param params filter parameters
     * @return list of tasks
     */
    List<TaskDTO> getAll(TaskParamsDTO params);

    /**
     * Returns a task by id.
     * @param id task id
     * @return task DTO
     */
    TaskDTO getById(Long id);

    /**
     * Creates a task.
     * @param dto create data
     * @return created task
     */
    TaskDTO create(TaskCreateDTO dto);

    /**
     * Updates a task partially.
     * @param id task id
     * @param dto update data
     * @return updated task
     */
    TaskDTO update(Long id, TaskUpdateDTO dto);

    /**
     * Deletes a task.
     * @param id task id
     */
    void delete(Long id);
}
