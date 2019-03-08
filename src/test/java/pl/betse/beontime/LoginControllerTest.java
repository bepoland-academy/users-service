package pl.betse.beontime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.betse.beontime.bo.UserBo;
import pl.betse.beontime.controller.LoginController;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.model.exception.UserBadCredentialException;
import pl.betse.beontime.service.UsersService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    private UsersService userService = Mockito.mock(UsersService.class);
    private PasswordEncoder passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    private LoginController loginController;
    private UserBo TEST_USER;
    private UserEntity TEST_USER_ENTITY;


    @BeforeEach
    void setup() {
//        TEST_USER = new UserBo("CustomGUID", "test@test.pl", "TestName", "TestLastName", false, "BANKING", new ArrayList<>());
//        TEST_USER_ENTITY = new UserEntity(1, "CustomGUID", TEST_USER.getEmail(), TEST_USER.getFirstName(), TEST_USER.getLastName(), "PASSWORD", TEST_USER.isActive(), new DepartmentEntity(1, "BANKING"), new HashSet<>());
        loginController = new LoginController(userService, passwordEncoder);
    }


    @Test
    void checkExsistsByEmailLogin() {
        when(userService.existsByEmail(TEST_USER.getEmail())).thenReturn(true);
        when(userService.getUserByEmail(TEST_USER.getEmail())).thenReturn(TEST_USER_ENTITY);

        assertThrows(UserBadCredentialException.class, () -> {
            loginController.checkUserCredentials(TEST_USER.getEmail(), TEST_USER_ENTITY.getPassword());
        });

    }

    @Test
    void checkIfUserEntityNotExist() {
        when(userService.existsByEmail(TEST_USER.getEmail())).thenReturn(true);
        when(userService.getUserByEmail(TEST_USER.getEmail())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> loginController.checkUserCredentials(TEST_USER.getEmail(), "PASSWORD"));
    }


    @Test
    void checkPasswordNotMatches() {
        when(userService.existsByEmail(TEST_USER.getEmail())).thenReturn(true);
        when(userService.getUserByEmail(TEST_USER.getEmail())).thenReturn(TEST_USER_ENTITY);
        when(passwordEncoder.matches("PASSWORD", "PASSWORD")).thenReturn(false);

        assertThrows(UserBadCredentialException.class, () -> loginController.checkUserCredentials(TEST_USER.getEmail(), "PASSWORD"));

    }

    @Test
    void checkLoginCredentialsResponse() {
        when(userService.existsByEmail(TEST_USER.getEmail())).thenReturn(true);
        when(passwordEncoder.matches(TEST_USER_ENTITY.getPassword(), TEST_USER_ENTITY.getPassword())).thenReturn(true);
        when(userService.getUserByEmail(TEST_USER.getEmail())).thenReturn(TEST_USER_ENTITY);

        assertEquals(TEST_USER, loginController.checkUserCredentials(TEST_USER.getEmail(), TEST_USER_ENTITY.getPassword()));

    }


}
