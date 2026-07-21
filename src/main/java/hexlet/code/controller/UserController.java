package hexlet.code.controller;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * REST controller for users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Returns all users.
     * @return list of users
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        return userService.getAll();
    }

    /**
     * Returns a user by id.
     * @param id user id
     * @return user
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable long id) {
        return userService.getById(id);
    }

    /**
     * Creates a user.
     * @param dto create data
     * @return created user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO dto) {
        return userService.create(dto);
    }

    /**
     * Updates a user. Only the owner can update.
     * @param id user id
     * @param dto update data
     * @return updated user
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@userUtils.isOwner(#id)")
    public UserDTO update(@PathVariable long id, @Valid @RequestBody UserUpdateDTO dto) {
        return userService.update(id, dto);
    }

    /**
     * Deletes a user. Only the owner can delete.
     * @param id user id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userUtils.isOwner(#id)")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
