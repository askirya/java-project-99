package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default task service implementation.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private static final String TASK_NOT_FOUND = "Task with id %s not found";

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;

    /**
     * Creates task service.
     * @param taskRepository tasks repository
     * @param taskMapper task mapper
     * @param taskSpecification task filter specification
     */
    public TaskServiceImpl(
            TaskRepository taskRepository,
            TaskMapper taskMapper,
            TaskSpecification taskSpecification
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskSpecification = taskSpecification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskDTO> getAll(TaskParamsDTO params) {
        return taskRepository.findAll(taskSpecification.build(params)).stream()
                .map(taskMapper::map)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskDTO getById(Long id) {
        return taskMapper.map(findTask(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskDTO create(TaskCreateDTO dto) {
        Task task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskDTO update(Long id, TaskUpdateDTO dto) {
        Task task = findTask(id);
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        taskRepository.delete(findTask(id));
    }

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND.formatted(id)));
    }
}
