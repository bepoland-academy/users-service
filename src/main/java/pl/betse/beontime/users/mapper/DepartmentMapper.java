package pl.betse.beontime.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;
import pl.betse.beontime.users.bo.DepartmentBo;
import pl.betse.beontime.users.entity.DepartmentEntity;

@Component
@Mapper(componentModel = "spring")
public abstract class DepartmentMapper {


    @Mappings({
            @Mapping(target = "id", source = "guid"),
            @Mapping(target = "name", source = "name")
    })
    public abstract DepartmentBo fromDepartmentEntity(DepartmentEntity departmentEntity);


    @Mappings({
            @Mapping(target = "guid", source = "id"),
            @Mapping(target = "name", source = "name")
    })
    public abstract DepartmentEntity fromDepartmentBo(DepartmentBo departmentBo);


}
