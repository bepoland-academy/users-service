package pl.betse.beontime.users.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.entity.RoleEntity;
import pl.betse.beontime.users.entity.UserEntity;
import pl.betse.beontime.users.model.UserBody;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    //FROM USER ENTITY TO USER BO
    @Mappings({
            @Mapping(target = "userId", source = "guid"),
            @Mapping(target = "department", source = "department.name")
    })
    public abstract UserBo mapFromUserEntity(UserEntity userEntity);

    @IterableMapping(elementTargetType = String.class, qualifiedByName = "mapRoleEntity")
    protected abstract List<String> mapRolesFromEntity(List<RoleEntity> roles);

    @Named("mapRoleEntity")
    String mapRoleEntityToString(RoleEntity roleEntity) {
        return roleEntity.getName();
    }


    //FROM USER BO TO USER ENTITY
    @Mappings({
            @Mapping(target = "guid", source = "userId"),
            @Mapping(target = "department.name", source = "department")
    })
    public abstract UserEntity mapFromUserBo(UserBo userBo);

    @IterableMapping(elementTargetType = RoleEntity.class, qualifiedByName = "mapRoleString")
    protected abstract List<RoleEntity> mapRolesFromString(List<String> roles);

    @Named("mapRoleString")
    RoleEntity mapStringToRoleEntity(String roleString) {
        return RoleEntity.builder().name(roleString).build();
    }

    //FROM USER BO TO USER REST
    public abstract UserBody mapFromUserBoToUser(UserBo userBo);

    public abstract List<UserBody> mapFromUserBosToUsers(List<UserBo> userBo);


    //FROM USER REST TO USER BO
    public abstract UserBo mapFromUserToUserBo(UserBody user);
}
