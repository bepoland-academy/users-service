package pl.betse.beontime.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.betse.beontime.users.bo.DepartmentBo;
import pl.betse.beontime.users.entity.DepartmentEntity;
import pl.betse.beontime.users.model.DepartmentBody;

@Mapper(componentModel = "spring", uses = GuidMapper.class)
public abstract class DepartmentMapper {

    @Mappings({
            @Mapping(target = "id", source = "guid")
    })
    public abstract DepartmentBo fromEntityToBo(DepartmentEntity departmentEntity);

    @Mappings(
            @Mapping(source = "id", target = "departmentId")
    )
    public abstract DepartmentBody fromBoToBody(DepartmentBo departmentBo);
}
