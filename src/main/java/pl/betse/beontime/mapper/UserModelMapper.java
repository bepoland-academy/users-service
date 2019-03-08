package pl.betse.beontime.mapper;

import org.springframework.stereotype.Component;
import pl.betse.beontime.bo.UserBo;
import pl.betse.beontime.entity.UserEntity;

@Component
public class UserModelMapper {

    public static UserBo fromUserEntityToUserDTO(UserEntity userEntity) {

//        List<String> userRoles = new ArrayList<>();
//
//        for (RoleEntity roleEntity : userEntity.getRoles()) {
//            userRoles.add(roleEntity.getRole());
//        }
//
//        return userEntity == null ? null : UserBo.builder()
//                .userId(userEntity.getUserGUID())
//                .email(userEntity.getEmailLogin())
//                .firstName(userEntity.getFirstName())
//                .lastName(userEntity.getLastName())
////                .password(userEntity.getPassword())
//                .active(userEntity.isActive())
//                .department(userEntity.getDepartment().getName())
//                .roles(userRoles)
//                .build();

        return null;
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
