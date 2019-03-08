package pl.betse.beontime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.betse.beontime.bo.DepartmentBo;
import pl.betse.beontime.controller.DepartmentController;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.model.exception.EmptyDepartmentListException;
import pl.betse.beontime.mapper.DepartmentModelMapper;
import pl.betse.beontime.service.DepartmentService;
import pl.betse.beontime.service.UsersService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DepartmentControllerTest {


    private UsersService usersService = Mockito.mock(UsersService.class);
    private DepartmentService departmentService = Mockito.mock(DepartmentService.class);

    private DepartmentController departmentController;
    private DepartmentEntity departmentEntity;
    private DepartmentBo departmentBO;

    @BeforeEach
    void setup() {
        departmentController = new DepartmentController(usersService, departmentService);
//        department = new DepartmentEntity(1, "SALESFORCE");
        departmentBO = DepartmentModelMapper.fromDepartmentEntityToDepartmentDTO(departmentEntity);
    }


    @Test
    void checkIfDepartmentListIsNotEmpty() {
        when(departmentService.findAll()).thenReturn(Collections.singletonList(departmentEntity));
        assertTrue(departmentController.getDepartmentWithUsers().size() > 0);
    }

    @Test
    void checkIfEmptyDepartmentListExceptionHasBeenThrown() {
        when(departmentService.findAll()).thenReturn(Collections.emptyList());
        assertThrows(EmptyDepartmentListException.class, () -> departmentController.getDepartmentWithUsers());
    }

    @Test
    void checkGetDepartmentByIdWithUsers() {
        when(departmentService.existsById(1)).thenReturn(true);
        when(departmentService.getDepartmentById(1)).thenReturn(departmentEntity);
        when(usersService.findByDepartment(departmentEntity)).thenReturn(Collections.emptyList());

//       assertEquals(departmentBO, departmentController.getDepartmentByIdWithUsers("1"));
    }


    @Test
    void checkDepartmentNotFoundExceptionHasBeenThrown() {
        when(departmentService.existsById(1)).thenReturn(false);
//        assertThrows(DepartmentNotFoundException.class, () -> departmentController.getDepartmentByIdWithUsers("1"));
    }
}
