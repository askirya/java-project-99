package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exception.ResourceAssociatedException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic for labels.
 */
@Service
public class LabelService {

    private static final String LABEL_NOT_FOUND = "Label with id %s not found";
    private static final String LABEL_HAS_TASKS = "Label with id %s is used by tasks and cannot be deleted";

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelMapper labelMapper;

    /**
     * Returns all labels.
     * @return list of labels
     */
    public List<LabelDTO> getAll() {
        return labelRepository.findAll().stream()
                .map(labelMapper::map)
                .toList();
    }

    /**
     * Returns a label by id.
     * @param id label id
     * @return label DTO
     */
    public LabelDTO getById(Long id) {
        return labelMapper.map(findLabel(id));
    }

    /**
     * Creates a label.
     * @param dto create data
     * @return created label
     */
    public LabelDTO create(LabelCreateDTO dto) {
        Label label = labelMapper.map(dto);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    /**
     * Updates a label partially.
     * @param id label id
     * @param dto update data
     * @return updated label
     */
    public LabelDTO update(Long id, LabelUpdateDTO dto) {
        Label label = findLabel(id);
        labelMapper.update(dto, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    /**
     * Deletes a label when it is not linked to tasks.
     * @param id label id
     */
    public void delete(Long id) {
        findLabel(id);
        if (taskRepository.existsByLabelsId(id)) {
            throw new ResourceAssociatedException(LABEL_HAS_TASKS.formatted(id));
        }
        labelRepository.deleteById(id);
    }

    private Label findLabel(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LABEL_NOT_FOUND.formatted(id)));
    }
}
