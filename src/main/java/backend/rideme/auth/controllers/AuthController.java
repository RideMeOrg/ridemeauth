package backend.rideme.auth.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import backend.rideme.auth.dto.converters.ApiResponse;
import backend.rideme.auth.dto.converters.LoginRequest;
import backend.rideme.auth.dto.converters.RegisterUser;
import backend.rideme.auth.services.AuthService;

import javax.transaction.Transactional;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "login")
    @Transactional
    public ResponseEntity<?> authenticateUser(@RequestBody @Validated @Valid LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping(value = "register")
    @Transactional
    public ApiResponse authenticateUser(@RequestBody @Validated @Valid RegisterUser registerUser) {
        return authService.createUser(registerUser);
    }

    @PutMapping(value = "register/confirm-account")
    @Transactional
    public ApiResponse confirmAccount(@RequestParam String token) {
        return authService.confirmAccount(token);
    }

}
