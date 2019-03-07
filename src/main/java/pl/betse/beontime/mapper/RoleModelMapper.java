package pl.betse.beontime.mapper;

import org.springframework.stereotype.Component;
import pl.betse.beontime.bo.RoleDTO;
import pl.betse.beontime.entity.RoleEntity;

@Component
public class RoleModelMapper {


    public static RoleDTO fromRoleEntityToRoleDto(RoleEntity roleEntity) {
        return roleEntity == null ? null : RoleDTO.builder()
                .id(roleEntity.getId())
                .role(roleEntity.getRole())
                .build();
    }

    public static RoleEntity fromRoleDtoToRoleEntity(RoleDTO roleDTO) {
        return roleDTO == null ? null : RoleEntity.builder()
                .id(roleDTO.getId())
                .role(roleDTO.getRole())
                .build();
    }


}
