package pl.betse.beontime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.betse.beontime.bo.UserBo;
import pl.betse.beontime.controller.UserController;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.model.exception.*;
import pl.betse.beontime.service.DepartmentService;
import pl.betse.beontime.service.RoleService;
import pl.betse.beontime.service.UsersService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UsersService usersService = Mockito.mock(UsersService.class);
    private RoleService roleService = Mockito.mock(RoleService.class);
    private DepartmentService departmentService = Mockito.mock(DepartmentService.class);

    private UserController userController;
    private PasswordEncoder passwordEncoder;
    private UserEntity userEntity;
    private UserBo userBO;
    private DepartmentEntity departmentEntity;
    private RoleEntity roleEntity;
    private List<RoleEntity> roleEntitySet;

    @BeforeEach
    void setup() {
        roleEntity = new RoleEntity(1, "ADMINISTRATION", new ArrayList<>());
        roleEntitySet = new ArrayList<>();
        roleEntitySet.add(roleEntity);
//        department = new DepartmentEntity(DepartmentEnum.DIGITAL);
//        userEntity = new UserEntity(1, "customGUID", "test@test.com", "test", "test", "password", true, department, roleEntitySet);
//        userBO = UserModelMapper.fromUserEntityToUserDTO(userEntity);
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
        when(usersService.existsById(1)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userController.getUserById("1"));
    }

    @Test
    void checkIfGetUserByIdReturnValue() {
        when(usersService.existsByGuid("customGUID")).thenReturn(true);
        when(usersService.findByGuid("customGUID")).thenReturn(userEntity);

        assertNotNull(userController.getUserById("customGUID"));

    }

    @Test
    void checkIfGetUserByIdReturnCorrectUserDTO() {
        when(usersService.existsByGuid("customGUID")).thenReturn(true);
        when(usersService.findByGuid("customGUID")).thenReturn(userEntity);

        assertEquals(userBO, userController.getUserById("customGUID"));

    }

    @Test
    void checkIfUserExistExceptionHasBeenThrown() {
        when(usersService.existsByEmail(userEntity.getEmail())).thenReturn(true);
        assertThrows(UserExistException.class, () -> userController.createNewUser(userBO));
    }


    @Test
    void checkIfDepartmentNotFoundExceptionHasBeenThrown() {
        when(usersService.existsByGuid("customGUID")).thenReturn(true);
        when(usersService.findByGuid("customGUID")).thenReturn(userEntity);
        userBO.setDepartment("AAA");
        assertThrows(DepartmentNotFoundException.class, () -> userController.updateUser("customGUID", userBO));

    }

    @Test
    void checkIfRoleNotFoundExceptionHasBeenThrown() {
        when(usersService.existsByGuid("customGUID")).thenReturn(true);
        when(usersService.findByGuid("customGUID")).thenReturn(userEntity);
        List<String> roles = new ArrayList<>();
        roles.add("NOROLE");
        userBO.setRoles(roles);
        assertThrows(RoleNotFoundException.class, () -> userController.updateUser("customGUID", userBO));
    }

}
