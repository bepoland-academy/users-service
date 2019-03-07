package pl.betse.beontime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.model.exception.UserNotFoundException;
import pl.betse.beontime.mapper.UserModelMapper;
import pl.betse.beontime.service.UsersService;

@RestController
public class TestController {

    @Autowired
    UsersService usersService;


    @RequestMapping(value = "/test/{GUID}", method = RequestMethod.GET)
    public UserDTO asd(@PathVariable("GUID") String guid) {
        if (!usersService.existsByGUID(guid)) {
            throw new UserNotFoundException();
        }

        return UserModelMapper.fromUserEntityToUserDTO(usersService.findByGUID(guid));
    }


}
