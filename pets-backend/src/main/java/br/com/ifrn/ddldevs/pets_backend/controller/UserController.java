package br.com.ifrn.ddldevs.pets_backend.controller;

import br.com.ifrn.ddldevs.pets_backend.dto.UserRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.UserResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.keycloak.KeycloakService;
import br.com.ifrn.ddldevs.pets_backend.service.UserService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO body) {
        return ResponseEntity.ok(userService.createUser(body));
//        return ResponseEntity.ok(keycloakService.createUser(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO body) {
        return ResponseEntity.ok(userService.updateUser(id, body));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
