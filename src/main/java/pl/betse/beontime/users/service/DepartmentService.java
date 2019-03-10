package pl.betse.beontime.users.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.betse.beontime.users.bo.DepartmentBo;
import pl.betse.beontime.users.exception.DepartmentNotFoundException;
import pl.betse.beontime.users.mapper.DepartmentMapper;
import pl.betse.beontime.users.repository.DepartmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public List<DepartmentBo> findAll() {
        List<DepartmentBo> departmentList = departmentRepository.findAll().stream()
                .map(departmentMapper::fromDepartmentEntity)
                .collect(Collectors.toList());
        if (departmentList.isEmpty()) {
            log.error("Empty department list.");
            throw new DepartmentNotFoundException();
        }
        return departmentList;
    }

}
