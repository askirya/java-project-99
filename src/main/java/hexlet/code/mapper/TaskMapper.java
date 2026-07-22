package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maps task DTOs to entity and back.
 * Field injection is required for MapStruct-generated Spring beans.
 */
@Mapper(
        uses = {JsonNullableMapper.class},
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@SuppressWarnings("java:S6813")
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusFromSlug")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "userFromId")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "labelsFromIds")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "idsFromLabels")
    public abstract TaskDTO map(Task task);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusFromSlug")
    @Mapping(target = "assignee", source = "assigneeId", qualifiedByName = "userFromId")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "labelsFromIds")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task task);

    /**
     * Resolves task status by slug.
     * @param slug status slug
     * @return task status
     */
    @Named("statusFromSlug")
    public TaskStatus statusFromSlug(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with slug " + slug + " not found"));
    }

    /**
     * Resolves user by id. Null id means no assignee.
     * @param id user id
     * @return user or null
     */
    @Named("userFromId")
    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    /**
     * Resolves labels by ids.
     * @param ids label ids
     * @return labels
     */
    @Named("labelsFromIds")
    public Set<Label> labelsFromIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(labelRepository.findAllById(ids));
    }

    /**
     * Maps labels to ids.
     * @param labels labels
     * @return ids
     */
    @Named("idsFromLabels")
    public Set<Long> idsFromLabels(Set<Label> labels) {
        if (labels == null || labels.isEmpty()) {
            return Collections.emptySet();
        }
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }
}
