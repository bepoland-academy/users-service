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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {


    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserServiceImpl userService;

    private DepartmentEntity simpleDepartment;
    private UserEntity simpleUser;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository);
        simpleDepartment = new DepartmentEntity(1, "BANKING");
        simpleUser = new UserEntity(1,"CustomGUID", "test@be-tse.com", "Test", "Test", "Password", false, simpleDepartment, null);
    }


    @Test
    void checkFindById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(simpleUser));
        assertNotNull(userService.findById(1));
    }

    @Test
    void checkIfFindAllWorks() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(simpleUser));
        assertTrue(userService.findAll().size() > 0);
    }

    @Test
    void checkExistsByEmailLogin() {
        when(userRepository.existsByEmailLogin("test@be-tse.com")).thenReturn(true);
        assertTrue(userService.existsByEmailLogin("test@be-tse.com"));
    }

    @Test
    void checkExistById() {
        when(userRepository.existsByUserId(1)).thenReturn(true);
        assertTrue(userService.existsByUserId(1));
    }

    @Test
    void checkFindByDepartmentEntity() {
        when(userRepository.findByDepartmentEntity(simpleDepartment)).thenReturn(Collections.singletonList(simpleUser));
        assertTrue(userService.findByDepartmentEntity(simpleDepartment).size() > 0);
    }

    @Test
    void checkGetUserByEmailNotNull() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(simpleUser);
        assertNotNull(userService.getUserByEmail("test@test.com"));
    }

    @Test
    void checkIfDeleteUserWorks() {
        userService.deleteById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void checkIfSaveUserWorks() {
        userService.save(simpleUser);
        verify(userRepository, times(1)).save(simpleUser);
    }


}
