package backend.rideme.auth.services;

import backend.rideme.auth.dto.converters.ApiResponse;
import backend.rideme.auth.dto.converters.LoginRequest;
import backend.rideme.auth.dto.converters.RegisterUser;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    ApiResponse createUser(RegisterUser registerUser);

    ApiResponse confirmAccount(String token);

}
