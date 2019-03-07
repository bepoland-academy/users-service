package pl.betse.beontime.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.model.exception.UserBadCredentialException;
import pl.betse.beontime.model.exception.UserNotFoundException;
import pl.betse.beontime.mapper.UserModelMapper;
import pl.betse.beontime.service.UsersService;

@RestController
@RequestMapping("/login")
@CrossOrigin("*")
public class LoginController {


    private UsersService usersService;
    private PasswordEncoder passwordEncoder;

    public LoginController(UsersService usersService, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
    }



    @GetMapping
    public @ResponseBody
    UserDTO checkUserCredentials(@RequestParam("email") String email, @RequestParam("pass") String password) {
        if (!usersService.existsByEmailLogin(email)) {
            throw new UserNotFoundException();
        }
        UserEntity userEntity = usersService.getUserByEmail(email);
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new UserBadCredentialException();
        }


        return UserModelMapper.fromUserEntityToUserDTO(userEntity);
    }

}
