package pl.betse.beontime.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.model.custom_exceptions.UserBadCredentialException;
import pl.betse.beontime.model.validation.LoginUserValidation;
import pl.betse.beontime.model_mapper.UserModelMapper;
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

    @PostMapping
    public @ResponseBody
    UserDTO checkUserCredentials(@RequestBody @Validated(LoginUserValidation.class) UserDTO incomingUserCredentials) {


        if (!usersService.existsByEmailLogin(incomingUserCredentials.getEmailLogin())) {
            throw new UserBadCredentialException();
        }

        UserEntity userEntity = usersService.getUserByEmail(incomingUserCredentials.getEmailLogin());

        if (!passwordEncoder.matches(incomingUserCredentials.getPassword(), userEntity.getPassword())) {
            throw new UserBadCredentialException();
        }

        return UserModelMapper.fromUserEntityToUserDTO(userEntity);
    }

}
