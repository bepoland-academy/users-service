package pl.betse.beontime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.controller.LoginController;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.model.custom_exceptions.UserBadCredentialException;
import pl.betse.beontime.service.UsersService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    private UsersService userService = Mockito.mock(UsersService.class);
    private PasswordEncoder passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    private LoginController loginController;
    private UserDTO TEST_USER;
    private UserEntity TEST_USER_ENTITY;


    @BeforeEach
    void setup() {
        TEST_USER = new UserDTO(1,"CustomGUID", "test@test.pl", "test", "test", "qwe123!", false, "BANKING", new HashSet<>());
        TEST_USER_ENTITY = new UserEntity(1,"CustomGUID", TEST_USER.getEmailLogin(), TEST_USER.getFirstName(), TEST_USER.getLastName(), TEST_USER.getPassword(), TEST_USER.isActive(), new DepartmentEntity(1, "BANKING"), new HashSet<>());
        loginController = new LoginController(userService, passwordEncoder);
    }


    @Test
    void checkExsistsByEmailLogin() {
        when(userService.existsByEmailLogin(TEST_USER.getEmailLogin())).thenReturn(false);

        assertThrows(UserBadCredentialException.class, () -> {
            loginController.checkUserCredentials(TEST_USER);
        });

    }

    @Test
    void checkIfUserEntityNotExist() {
        when(userService.existsByEmailLogin(TEST_USER.getEmailLogin())).thenReturn(true);
        when(userService.getUserByEmail(TEST_USER.getEmailLogin())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> loginController.checkUserCredentials(TEST_USER));
    }


    @Test
    void checkPasswordNotMatches() {
        when(userService.existsByEmailLogin(TEST_USER.getEmailLogin())).thenReturn(true);
        when(userService.getUserByEmail(TEST_USER.getEmailLogin())).thenReturn(TEST_USER_ENTITY);
        when(passwordEncoder.matches(TEST_USER.getPassword(), TEST_USER.getPassword())).thenReturn(false);

        assertThrows(UserBadCredentialException.class, () -> loginController.checkUserCredentials(TEST_USER));

    }

    @Test
    void checkLoginCredentialsResponse() {
        when(userService.existsByEmailLogin(TEST_USER.getEmailLogin())).thenReturn(true);
        when(passwordEncoder.matches(TEST_USER.getPassword(), TEST_USER.getPassword())).thenReturn(true);
        when(userService.getUserByEmail(TEST_USER.getEmailLogin())).thenReturn(TEST_USER_ENTITY);

        assertEquals(TEST_USER, loginController.checkUserCredentials(TEST_USER));

    }


}
