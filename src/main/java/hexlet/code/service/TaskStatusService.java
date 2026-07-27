package hexlet.code.service;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;

import java.util.List;

/**
 * Task status business operations.
 */
public interface TaskStatusService {

    /**
     * Returns all task statuses.
     * @return list of statuses
     */
    List<TaskStatusDTO> getAll();

    /**
     * Returns a task status by id.
     * @param id status id
     * @return status DTO
     */
    TaskStatusDTO getById(Long id);

    /**
     * Creates a task status.
     * @param dto create data
     * @return created status
     */
    TaskStatusDTO create(TaskStatusCreateDTO dto);

    /**
     * Updates a task status partially.
     * @param id status id
     * @param dto update data
     * @return updated status
     */
    TaskStatusDTO update(Long id, TaskStatusUpdateDTO dto);

    /**
     * Deletes a task status.
     * @param id status id
     */
    void delete(Long id);
}
