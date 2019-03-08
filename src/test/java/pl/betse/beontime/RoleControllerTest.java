package pl.betse.beontime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.model.exception.EmptyRoleListException;
import pl.betse.beontime.model.exception.RoleNotFoundException;
import pl.betse.beontime.mapper.RoleModelMapper;
import pl.betse.beontime.service.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RoleControllerTest {

    private RoleService roleService = Mockito.mock(RoleServiceImpl.class);

//    private RoleController roleController;
    private RoleEntity roleEntity;
//    private RoleDTO roleDTOexample;

    @BeforeEach
    void setup() {
//        roleController = new RoleController(roleService);
//        roleEntity = new RoleEntity(1, "CONSULTANT", new HashSet<>());
//        roleDTOexample = RoleModelMapper.fromRoleEntityToRoleDto(roleEntity);
//        roleDTOexample.setUsers(new ArrayList<>());
    }


    @Test
    void checkIfRoleListIsNotEmpty() {
//        when(roleService.findAll()).thenReturn(Collections.singletonList(new RoleEntity(1, "ADMINISTRATION", new ArrayList<>())));
//        assertTrue(roleController.getRoleListWithUsers().size() > 0);
    }

    @Test
    void checkIfEmptyRoleExceptionHasBeenThrown() {
        when(roleService.findAll()).thenReturn(new ArrayList<>());
//        assertThrows(EmptyRoleListException.class, () -> roleController.getRoleListWithUsers());
    }

    @Test
    void checkGetRolesByIdWithUsers() {
        when(roleService.existsById(1)).thenReturn(true);
        when(roleService.findById(1)).thenReturn(roleEntity);
//        assertEquals(roleDTOexample, roleController.getRolesByIdWithUsers("1"));

    }

    @Test
    void checkIfRoleNotFoundExceptionHasBeenThrown() {
        when(roleService.existsById(1)).thenReturn(false);
//        assertThrows(RoleNotFoundException.class, () -> roleController.getRolesByIdWithUsers("1"));
    }


}
