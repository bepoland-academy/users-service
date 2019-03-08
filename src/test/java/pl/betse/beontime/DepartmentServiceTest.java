package pl.betse.beontime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.repository.DepartamentRepository;
import pl.betse.beontime.service.DepartmentServiceImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class DepartmentServiceTest {

    private DepartamentRepository departamentRepository = Mockito.mock(DepartamentRepository.class);
    private DepartmentServiceImpl departmentService;

    private DepartmentEntity simpleDepartment;

    @BeforeEach
    void setup() {
        departmentService = new DepartmentServiceImpl(departamentRepository);
//        simpleDepartment = new DepartmentEntity(1, "BANKING");
    }

    @Test
    void checkFindById(){
        when(departamentRepository.findById(1)).thenReturn(Optional.of(simpleDepartment));
        assertNotNull(departmentService.getDepartmentById(1));
    }

    @Test
    void checkFindByName(){
        when(departamentRepository.findByName("BANKING")).thenReturn(simpleDepartment);
        assertNotNull(departmentService.findByName("BANKING"));
        assertEquals(departmentService.findByName(simpleDepartment.getName()),simpleDepartment);
    }

    @Test
    void checkFindAllWorks() {
        when(departamentRepository.findAll()).thenReturn(Collections.singletonList(simpleDepartment));
        assertTrue(departmentService.findAll().size() > 0);
    }

    @Test
    void checkIfDepartmentSaveWorks() {
        departmentService.save(simpleDepartment);
        verify(departamentRepository, times(1)).save(simpleDepartment);
    }
}
