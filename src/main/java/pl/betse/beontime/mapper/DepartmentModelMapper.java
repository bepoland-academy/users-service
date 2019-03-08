package pl.betse.beontime.mapper;

import org.springframework.stereotype.Component;
import pl.betse.beontime.bo.DepartmentBo;
import pl.betse.beontime.entity.DepartmentEntity;

@Component
public class DepartmentModelMapper {

    public static DepartmentBo fromDepartmentEntityToDepartmentDTO(DepartmentEntity departmentEntity) {
//        return department == null ? null : DepartmentBo.builder()
//                .guid(String.valueOf(department.getId()))
//                .name(department.getName())
//                .build();
        return null;
    }

    public static DepartmentEntity fromDepartmentDtoToDepartmentEntity(DepartmentBo departmentBO) {
//        return departmentBO == null ? null : DepartmentEntity.builder()
//                .id(Integer.valueOf(departmentBO.getGuid()))
//                .name(departmentBO.getName())
//                .build();
        return null;
    }
}
