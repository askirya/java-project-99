package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for labels.
 */
@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * Returns all labels.
     * @return list of labels
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LabelDTO> index() {
        return labelService.getAll();
    }

    /**
     * Returns a label by id.
     * @param id label id
     * @return label
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable long id) {
        return labelService.getById(id);
    }

    /**
     * Creates a label.
     * @param dto create data
     * @return created label
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO dto) {
        return labelService.create(dto);
    }

    /**
     * Updates a label.
     * @param id label id
     * @param dto update data
     * @return updated label
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@PathVariable long id, @Valid @RequestBody LabelUpdateDTO dto) {
        return labelService.update(id, dto);
    }

    /**
     * Deletes a label.
     * @param id label id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        labelService.delete(id);
    }
}
