package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default label service implementation.
 */
@Service
public class LabelServiceImpl implements LabelService {

    private static final String LABEL_NOT_FOUND = "Label with id %s not found";

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    /**
     * Creates label service.
     * @param labelRepository labels repository
     * @param labelMapper label mapper
     */
    public LabelServiceImpl(LabelRepository labelRepository, LabelMapper labelMapper) {
        this.labelRepository = labelRepository;
        this.labelMapper = labelMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LabelDTO> getAll() {
        return labelRepository.findAll().stream()
                .map(labelMapper::map)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LabelDTO getById(Long id) {
        return labelMapper.map(findLabel(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LabelDTO create(LabelCreateDTO dto) {
        Label label = labelMapper.map(dto);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LabelDTO update(Long id, LabelUpdateDTO dto) {
        Label label = findLabel(id);
        labelMapper.update(dto, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        labelRepository.delete(findLabel(id));
    }

    private Label findLabel(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LABEL_NOT_FOUND.formatted(id)));
    }
}
