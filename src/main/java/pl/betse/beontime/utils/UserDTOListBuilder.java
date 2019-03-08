package pl.betse.beontime.utils;

import org.springframework.stereotype.Component;
import pl.betse.beontime.bo.UserBo;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.mapper.UserModelMapper;

import java.util.List;

@Component
public class UserDTOListBuilder {

    public static void build(List<UserBo> usersList, UserEntity userEntity) {
        UserBo userBO = UserModelMapper.fromUserEntityToUserDTO(userEntity);
        usersList.add(userBO);
    }
}
