package pl.betse.beontime.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.model_mapper.UserModelMapper;

import java.util.HashSet;
import java.util.UUID;

@RestController
public class TestController {


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public UserDTO asd() {

        String uuid = UUID.randomUUID().toString();

        UserDTO userDTO = UserModelMapper.fromUserEntityToUserDTO(new UserEntity(1, uuid, "email@wp.pl", "First", "Last", "Pass", false, new DepartmentEntity(1,"asd"), new HashSet<>()));

        return userDTO;
    }

}
