package pl.betse.beontime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.controller.UserController;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.model.custom_exceptions.*;
import pl.betse.beontime.model.enums.DepartmentEnum;
import pl.betse.beontime.model_mapper.UserModelMapper;
import pl.betse.beontime.service.DepartmentService;
import pl.betse.beontime.service.RoleService;
import pl.betse.beontime.service.UsersService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UsersService usersService = Mockito.mock(UsersService.class);
    private RoleService roleService = Mockito.mock(RoleService.class);
    private DepartmentService departmentService = Mockito.mock(DepartmentService.class);

    private UserController userController;
    private PasswordEncoder passwordEncoder;
    private UserEntity userEntity;
    private UserDTO userDTO;
    private DepartmentEntity departmentEntity;
    private RoleEntity roleEntity;
    private Set<RoleEntity> roleEntitySet;

    @BeforeEach
    void setup() {
        roleEntity = new RoleEntity(1, "ADMINISTRATION", new HashSet<>());
        roleEntitySet = new HashSet<>();
        roleEntitySet.add(roleEntity);
        departmentEntity = new DepartmentEntity(DepartmentEnum.DIGITAL);
        userEntity = new UserEntity(1,"customGUID", "test@test.com", "test", "test", "password", true, departmentEntity, roleEntitySet);
        userDTO = UserModelMapper.fromUserEntityToUserDTO(userEntity);
        passwordEncoder = new BCryptPasswordEncoder();
        userController = new UserController(usersService, departmentService, roleService, passwordEncoder);
    }


    @Test
    void checkObtainAllUsersListIsNotEmpty() {
        when(usersService.findAll()).thenReturn(Collections.singletonList(userEntity));
        assertTrue(userController.obtainAllUsers().size() > 0);
    }

    @Test
    void checkIfEmptyUserListExceptionHasBeenThrown() {
        when(usersService.findAll()).thenReturn(Collections.emptyList());
        assertThrows(EmptyUserListException.class, () -> userController.obtainAllUsers());
    }

    @Test
    void checkIfUserNotFoundExceptionHasBeenThrown() {
        when(usersService.existsByUserId(1)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userController.getUserById("1"));
    }

    @Test
    void checkIfGetUserByIdReturnValue() {
        when(usersService.existsByUserId(1)).thenReturn(true);
        when(usersService.findById(1)).thenReturn(userEntity);

        assertNotNull(userController.getUserById("1"));

    }

    @Test
    void checkIfGetUserByIdReturnCorrectUserDTO() {
        when(usersService.existsByUserId(1)).thenReturn(true);
        when(usersService.findById(1)).thenReturn(userEntity);

        assertEquals(userDTO, userController.getUserById("1"));

    }

    @Test
    void checkIfUserExistExceptionHasBeenThrown() {
        when(usersService.existsByEmailLogin(userEntity.getEmailLogin())).thenReturn(true);
        assertThrows(UserExistException.class, () -> userController.createNewUser(userDTO));
    }



    @Test
    void checkIfDepartmentNotFoundExceptionHasBeenThrown() {
        when(usersService.existsByUserId(1)).thenReturn(true);
        when(usersService.findById(1)).thenReturn(userEntity);
        userDTO.setDepartment("AAA");
        assertThrows(DepartmentNotFoundException.class, () -> userController.updateUser("1", userDTO));

    }

    @Test
    void checkIfRoleNotFoundExceptionHasBeenThrown() {
        when(usersService.existsByUserId(1)).thenReturn(true);
        when(usersService.findById(1)).thenReturn(userEntity);
        Set<String> roles = new HashSet<>();
        roles.add("NOROLE");
        userDTO.setRoles(roles);
        assertThrows(RoleNotFoundException.class, () -> userController.updateUser("1", userDTO));
    }

}
