package pl.betse.beontime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.repository.RoleRepository;
import pl.betse.beontime.service.RoleServiceImpl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    private RoleServiceImpl roleService;

    private RoleEntity simpleRoleEntity;
    private UserEntity simpleUser;

    @BeforeEach
    void setup() {
        roleService = new RoleServiceImpl(roleRepository);
        Set<UserEntity> setTest = new HashSet();
        simpleUser = new UserEntity(1,"CustomGUID", "test@be-tse.com", "Test", "Test", "Password", false, null, null);
        simpleRoleEntity = new RoleEntity(1, "CONSULTANT", setTest);
    }

    @Test
    void checkFindById() {
        when(roleRepository.findById(1)).thenReturn(Optional.of(simpleRoleEntity));
        assertNotNull(roleService.findById(1));

    }

    @Test
    void checkFindAll() {
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(simpleRoleEntity));
        assertTrue(roleService.findAll().size() > 0);
    }

    @Test
    void checkFindByName() {
        when(roleRepository.findByRole("CONSULTANT")).thenReturn(simpleRoleEntity);
        assertNotNull(roleService.findByName("CONSULTANT"));
        assertEquals(roleService.findByName(simpleRoleEntity.getRole()), simpleRoleEntity);
    }

    @Test
    void checkIfSaveRoleWork(){
        roleService.save(simpleRoleEntity);
        verify(roleRepository,times(1)).save(simpleRoleEntity);
    }


}
