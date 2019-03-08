package pl.betse.beontime.controller;

import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.bo.DepartmentBo;
import pl.betse.beontime.bo.UserBo;
import pl.betse.beontime.entity.UserEntity;
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
    public List<DepartmentBo> getDepartmentWithUsers() {

        List<DepartmentBo> departmentList = new ArrayList<>();
        departmentService.findAll();

        if (departmentList.isEmpty()) {
            throw new EmptyDepartmentListException();
        }

        return departmentList;
    }


}
