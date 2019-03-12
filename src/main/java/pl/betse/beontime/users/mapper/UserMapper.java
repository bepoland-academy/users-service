package pl.betse.beontime.users.mapper;

import org.mapstruct.*;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.entity.RoleEntity;
import pl.betse.beontime.users.entity.UserEntity;
import pl.betse.beontime.users.model.UserBody;

import java.util.List;

@Mapper(componentModel = "spring", uses = GuidMapper.class)
public abstract class UserMapper {

    @Mappings({
            @Mapping(target = "userId", source = "guid"),
            @Mapping(target = "department", source = "department.name")
    })
    public abstract UserBo fromEntityToBo(UserEntity userEntity);

    @IterableMapping(elementTargetType = String.class, qualifiedByName = "mapRoleEntity")
    protected abstract List<String> mapRolesFromEntity(List<RoleEntity> roles);

    @Named("mapRoleEntity")
    String mapRoleEntityToString(RoleEntity roleEntity) {
        return roleEntity.getName();
    }

    @Mappings({
            @Mapping(target = "guid", source = "userId", qualifiedByName = "mapGuid"),
            @Mapping(target = "department.name", source = "department")
    })
    public abstract UserEntity fromBoToEntity(UserBo userBo);

    @IterableMapping(elementTargetType = RoleEntity.class, qualifiedByName = "mapRoleString")
    protected abstract List<RoleEntity> mapRolesFromString(List<String> roles);

    @Named("mapRoleString")
    RoleEntity mapStringToRoleEntity(String roleString) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleString);
        return roleEntity;
    }

    public abstract UserBody fromBoToBody(UserBo userBo);

    public abstract UserBo mapFromUserToUserBo(UserBody user);
}
