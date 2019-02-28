package pl.betse.beontime;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.repository.UserRepository;
import pl.betse.beontime.service.UserServiceImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UsersServiceTest {


    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder;
    private DepartmentEntity simpleDepartment;
    private UserEntity simpleUser;
    private UserEntity anotherUser;

    @BeforeEach
    void init() {
        userService = new UserServiceImpl(userRepository);
        passwordEncoder = new BCryptPasswordEncoder();
        simpleDepartment = new DepartmentEntity(1, "BANKING");
        simpleUser = new UserEntity(1, "test@be-tse.com", "Test", "Test", "Password", false, simpleDepartment, null);
        anotherUser = new UserEntity(2, "test@be-tse.com", "Test", "Test", "Password", false, simpleDepartment, null);
    }


    @Test
    void checkFindById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(simpleUser));

        anotherUser.setUserId(1);

        assertEquals(userService.findById(1), anotherUser);
    }

    @Test
    void checkExistsByEmailLogin() {
        when(userRepository.existsByEmailLogin("test@be-tse.com")).thenReturn(true);
        assertTrue(userService.existsByEmailLogin("test@be-tse.com"));
    }

    @Test
    void checkFindByDepartmentEntity() {
        when(userRepository.findByDepartmentEntity(simpleDepartment)).thenReturn(Collections.singletonList(simpleUser));
        assertTrue(userService.findByDepartmentEntity(simpleDepartment).size() > 0);
    }

    @Test
    void checkUserParameters(){



    }





}
