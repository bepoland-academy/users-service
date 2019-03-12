package pl.betse.beontime.users.controller;

import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.betse.beontime.users.mapper.DepartmentMapper;
import pl.betse.beontime.users.model.DepartmentBody;
import pl.betse.beontime.users.service.DepartmentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departments")
@CrossOrigin("*")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @GetMapping
    public ResponseEntity<Resources<DepartmentBody>> getDepartments() {
        List<DepartmentBody> departments = departmentService.findAll().stream()
                .map(departmentMapper::fromBoToBody)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Resources<>(departments));
    }
}
