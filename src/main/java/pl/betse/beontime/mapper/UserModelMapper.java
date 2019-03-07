package pl.betse.beontime.mapper;

import org.springframework.stereotype.Component;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.entity.UserEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserModelMapper {

    public static UserDTO fromUserEntityToUserDTO(UserEntity userEntity) {

        List<String> userRoles = new ArrayList<>();

        for (RoleEntity roleEntity : userEntity.getRoles()) {
            userRoles.add(roleEntity.getRole());
        }

        return userEntity == null ? null : UserDTO.builder()
                .userId(userEntity.getUserGUID())
                .email(userEntity.getEmailLogin())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
//                .password(userEntity.getPassword())
                .active(userEntity.isActive())
                .department(userEntity.getDepartmentEntity().getName())
                .roles(userRoles)
                .build();
    }

    public static UserEntity fromUserDtoToUserEntity(UserDTO userDTO) {


        return userDTO == null ? null : UserEntity.builder()
                .emailLogin(userDTO.getEmail())
                .userGUID(userDTO.getUserId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .isActive(userDTO.isActive())
                .build();
    }


}
