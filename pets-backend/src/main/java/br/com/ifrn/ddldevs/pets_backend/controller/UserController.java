package br.com.ifrn.ddldevs.pets_backend.controller;

import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.User.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.User.UserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.User.UserUpdateRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.keycloak.KeycloakService;
import br.com.ifrn.ddldevs.pets_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name="Users", description = "API for Users management")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "List users")
    @GetMapping("/")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create new user")
    @PostMapping("/")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO body) {
        return ResponseEntity.ok(userService.createUser(body));
    }

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequestDTO body) {
        return ResponseEntity.ok(userService.updateUser(id, body));
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all pets of a user")
    @GetMapping("/{id}/pets")
    public ResponseEntity<List<PetResponseDTO>> getPets(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getPets(id));
    }
}
