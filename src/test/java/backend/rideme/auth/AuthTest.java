package backend.rideme.auth;

import backend.rideme.auth.dto.converters.ApiResponse;
import backend.rideme.auth.dto.converters.RegisterUser;
import backend.rideme.auth.services.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AuthTest {

    @Autowired
    private AuthService authService;


    @Test
    public void createUser() {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setUsername("kratos");
        registerUser.setFirstName("Djouko");
        registerUser.setLastName("Socrate");
        registerUser.setEmail("destructeurkratos@gmail.com");
        registerUser.setPassword("mot2P@ss");
        registerUser.setConfirmPassword("mot2P@ss");

        ApiResponse apiResponse = this.authService.createUser(registerUser);
        Assertions.assertThat(apiResponse.getSuccess()).isEqualTo(true);
    }
}
