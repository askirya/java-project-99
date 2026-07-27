package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;

import java.util.List;

/**
 * Label business operations.
 */
public interface LabelService {

    /**
     * Returns all labels.
     * @return list of labels
     */
    List<LabelDTO> getAll();

    /**
     * Returns a label by id.
     * @param id label id
     * @return label DTO
     */
    LabelDTO getById(Long id);

    /**
     * Creates a label.
     * @param dto create data
     * @return created label
     */
    LabelDTO create(LabelCreateDTO dto);

    /**
     * Updates a label partially.
     * @param id label id
     * @param dto update data
     * @return updated label
     */
    LabelDTO update(Long id, LabelUpdateDTO dto);

    /**
     * Deletes a label.
     * @param id label id
     */
    void delete(Long id);
}
