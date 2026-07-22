package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic for tasks.
 */
@Service
public class TaskService {

    private static final String TASK_NOT_FOUND = "Task with id %s not found";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    /**
     * Returns all tasks.
     * @return list of tasks
     */
    public List<TaskDTO> getAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::map)
                .toList();
    }

    /**
     * Returns a task by id.
     * @param id task id
     * @return task DTO
     */
    public TaskDTO getById(Long id) {
        return taskMapper.map(findTask(id));
    }

    /**
     * Creates a task.
     * @param dto create data
     * @return created task
     */
    public TaskDTO create(TaskCreateDTO dto) {
        Task task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    /**
     * Updates a task partially.
     * @param id task id
     * @param dto update data
     * @return updated task
     */
    public TaskDTO update(Long id, TaskUpdateDTO dto) {
        Task task = findTask(id);
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    /**
     * Deletes a task.
     * @param id task id
     */
    public void delete(Long id) {
        taskRepository.delete(findTask(id));
    }

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND.formatted(id)));
    }
}
