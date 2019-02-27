package pl.betse.beontime.model_mapper;

import org.springframework.stereotype.Component;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.entity.UserEntity;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserModelMapper {

    public static UserDTO fromUserEntityToUserDTO(UserEntity userEntity) {

        Set<String> userRoles = new HashSet<>();

        for (RoleEntity roleEntity : userEntity.getRoles()) {
            userRoles.add(roleEntity.getRole());
        }

        return userEntity == null ? null : UserDTO.builder()
                .userId(userEntity.getUserId())
                .emailLogin(userEntity.getEmailLogin())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .password(userEntity.getPassword())
                .isActive(userEntity.isActive())
                .department(userEntity.getDepartmentEntity().getName())
                .roles(userRoles)
                .build();
    }

    public static UserEntity fromUserDtoToUserEntity(UserDTO userDTO) {


        return userDTO == null ? null : UserEntity.builder()
                .emailLogin(userDTO.getEmailLogin())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .isActive(userDTO.isActive())
                .build();
    }


}
