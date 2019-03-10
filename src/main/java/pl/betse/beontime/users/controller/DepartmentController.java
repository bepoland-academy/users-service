package pl.betse.beontime.users.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.betse.beontime.users.bo.DepartmentBo;
import pl.betse.beontime.users.exception.EmptyDepartmentListException;
import pl.betse.beontime.users.service.DepartmentService;
import pl.betse.beontime.users.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/departments")
@CrossOrigin("*")
public class DepartmentController {


    private UserService userService;
    private DepartmentService departmentService;

    public DepartmentController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<DepartmentBo> getDepartmentWithUsers() {
        List<DepartmentBo> departmentList = departmentService.findAll();
        if (departmentList.isEmpty()) {
            throw new EmptyDepartmentListException();
        }
        return departmentList;
    }


}
