package hexlet.code.service;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceAssociatedException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic for task statuses.
 */
@Service
public class TaskStatusService {

    private static final String STATUS_NOT_FOUND = "Task status with id %s not found";
    private static final String STATUS_HAS_TASKS = "Task status with id %s is used by tasks and cannot be deleted";

    private final TaskStatusRepository taskStatusRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusMapper taskStatusMapper;

    /**
     * Creates task status service.
     * @param taskStatusRepository statuses repository
     * @param taskRepository tasks repository
     * @param taskStatusMapper status mapper
     */
    public TaskStatusService(
            TaskStatusRepository taskStatusRepository,
            TaskRepository taskRepository,
            TaskStatusMapper taskStatusMapper
    ) {
        this.taskStatusRepository = taskStatusRepository;
        this.taskRepository = taskRepository;
        this.taskStatusMapper = taskStatusMapper;
    }

    /**
     * Returns all task statuses.
     * @return list of statuses
     */
    public List<TaskStatusDTO> getAll() {
        return taskStatusRepository.findAll().stream()
                .map(taskStatusMapper::map)
                .toList();
    }

    /**
     * Returns a task status by id.
     * @param id status id
     * @return status DTO
     */
    public TaskStatusDTO getById(Long id) {
        return taskStatusMapper.map(findStatus(id));
    }

    /**
     * Creates a task status.
     * @param dto create data
     * @return created status
     */
    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        TaskStatus status = taskStatusMapper.map(dto);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    /**
     * Updates a task status partially.
     * @param id status id
     * @param dto update data
     * @return updated status
     */
    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO dto) {
        TaskStatus status = findStatus(id);
        taskStatusMapper.update(dto, status);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    /**
     * Deletes a task status.
     * @param id status id
     */
    public void delete(Long id) {
        findStatus(id);
        if (taskRepository.existsByTaskStatusId(id)) {
            throw new ResourceAssociatedException(STATUS_HAS_TASKS.formatted(id));
        }
        taskStatusRepository.deleteById(id);
    }

    private TaskStatus findStatus(Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STATUS_NOT_FOUND.formatted(id)));
    }
}
