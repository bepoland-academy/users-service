package pl.betse.beontime.controller;

import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.bo.DepartmentDTO;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.model.exception.DepartmentNotFoundException;
import pl.betse.beontime.model.exception.EmptyDepartmentListException;
import pl.betse.beontime.mapper.DepartmentModelMapper;
import pl.betse.beontime.service.DepartmentService;
import pl.betse.beontime.service.UsersService;
import pl.betse.beontime.utils.UserDTOListBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/departments")
@CrossOrigin("*")
public class DepartmentController {


    private UsersService usersService;
    private DepartmentService departmentService;

    public DepartmentController(UsersService usersService, DepartmentService departmentService) {
        this.usersService = usersService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<DepartmentDTO> getDepartmentWithUsers() {

        List<DepartmentDTO> departmentList = new ArrayList<>();
        departmentService.findAll().forEach(department -> {

            List<UserDTO> userList = new ArrayList<>();
            for (UserEntity user : usersService.findByDepartmentEntity(departmentService.getDepartmentById(department.getId()))) {
                UserDTOListBuilder.build(userList, user);
            }

            DepartmentDTO departmentDTO = DepartmentModelMapper.fromDepartmentEntityToDepartmentDTO(department);
            departmentDTO.setUsers(userList);
            departmentList.add(departmentDTO);
        });

        if (departmentList.isEmpty()) {
            throw new EmptyDepartmentListException();
        }

        return departmentList;
    }

    @GetMapping(path = "/{id}")
    public DepartmentDTO getDepartmentByIdWithUsers(@PathVariable("id") String departmentId) {

        if (!departmentService.existsById(Integer.valueOf(departmentId))) {
            throw new DepartmentNotFoundException();
        }

        DepartmentDTO departmentDTO = DepartmentModelMapper.fromDepartmentEntityToDepartmentDTO(departmentService.getDepartmentById(Integer.valueOf(departmentId)));

        List<UserDTO> userList = new ArrayList<>();
        for (UserEntity user : usersService.findByDepartmentEntity(departmentService.getDepartmentById(departmentDTO.getId()))) {
            UserDTOListBuilder.build(userList, user);
        }

        departmentDTO.setUsers(userList);

        return departmentDTO;
    }


}
