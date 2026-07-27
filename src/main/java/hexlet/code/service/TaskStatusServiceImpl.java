package hexlet.code.service;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default task status service implementation.
 */
@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    private static final String STATUS_NOT_FOUND = "Task status with id %s not found";

    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    /**
     * Creates task status service.
     * @param taskStatusRepository statuses repository
     * @param taskStatusMapper status mapper
     */
    public TaskStatusServiceImpl(
            TaskStatusRepository taskStatusRepository,
            TaskStatusMapper taskStatusMapper
    ) {
        this.taskStatusRepository = taskStatusRepository;
        this.taskStatusMapper = taskStatusMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskStatusDTO> getAll() {
        return taskStatusRepository.findAll().stream()
                .map(taskStatusMapper::map)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskStatusDTO getById(Long id) {
        return taskStatusMapper.map(findStatus(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        TaskStatus status = taskStatusMapper.map(dto);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO dto) {
        TaskStatus status = findStatus(id);
        taskStatusMapper.update(dto, status);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        taskStatusRepository.delete(findStatus(id));
    }

    private TaskStatus findStatus(Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STATUS_NOT_FOUND.formatted(id)));
    }
}
