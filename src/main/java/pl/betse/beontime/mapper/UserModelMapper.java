package pl.betse.beontime.mapper;

import org.springframework.stereotype.Component;
import pl.betse.beontime.bo.UserBo;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserModelMapper {

    public static UserBo fromUserEntityToUserDTO(UserEntity userEntity) {

        List<String> roles = new ArrayList<>();

        for (RoleEntity roleEntity : userEntity.getRoles()) {
            roles.add(roleEntity.getName());
        }

        return UserBo.builder()
                        .id(userEntity.getGuid())
                        .email(userEntity.getEmail())
                        .firstName(userEntity.getFirstName())
                        .lastName(userEntity.getLastName())
                        .active(userEntity.isActive())
                        .department(userEntity.getDepartment().getName())
                        .roles(roles)
                        .build();
    }

    public static UserEntity fromUserDtoToUserEntity(UserBo userBO) {

//
//        return userBO == null ? null : UserEntity.builder()
//                .emailLogin(userBO.getEmail())
//                .userGUID(userBO.getUserId())
//                .firstName(userBO.getFirstName())
//                .lastName(userBO.getLastName())
//                .isActive(userBO.isActive())
//                .build();

        return null;
    }


}
