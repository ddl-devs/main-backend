package br.com.ifrn.ddldevs.pets_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/")
    @PreAuthorize("hasRole('user')")
    public String list() {
        return "Listandoooo...";
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('admin')")
    public String create() {
        return "Criandoooo...";
    }
}
