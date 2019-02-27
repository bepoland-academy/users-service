package pl.betse.beontime.model_mapper;

import org.springframework.stereotype.Component;
import pl.betse.beontime.bo.DepartmentDTO;
import pl.betse.beontime.entity.DepartmentEntity;

@Component
public class DepartmentModelMapper {

    public static DepartmentDTO fromDepartmentEntityToDepartmentDTO(DepartmentEntity departmentEntity) {
        return departmentEntity == null ? null : DepartmentDTO.builder()
                .id(departmentEntity.getId())
                .name(departmentEntity.getName())
                .build();
    }

    public static DepartmentEntity fromDepartmentDtoToDepartmentEntity(DepartmentDTO departmentDTO) {
        return departmentDTO == null ? null : DepartmentEntity.builder()
                .id(departmentDTO.getId())
                .name(departmentDTO.getName())
                .build();
    }
}
