package pl.betse.beontime.utils;

import org.springframework.stereotype.Component;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.model_mapper.UserModelMapper;

import java.util.List;

@Component
public class UserDTOListBuilder {

    public static void build(List<UserDTO> usersList, UserEntity userEntity) {
        UserDTO userDTO = UserModelMapper.fromUserEntityToUserDTO(userEntity);
        usersList.add(userDTO);
    }
}
