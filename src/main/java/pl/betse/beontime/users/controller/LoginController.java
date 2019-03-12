package pl.betse.beontime.users.controller;

import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.users.mapper.UserMapper;
import pl.betse.beontime.users.model.UserBody;
import pl.betse.beontime.users.service.LoginService;
import pl.betse.beontime.users.service.PasswordService;
import pl.betse.beontime.users.service.UserService;

@RestController
@RequestMapping("/login")
@CrossOrigin("*")
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;
    private final PasswordService passwordService;
    private final UserMapper userMapper;

    public LoginController(LoginService loginService, UserService userService, PasswordService passwordService, UserMapper userMapper) {
        this.loginService = loginService;
        this.userService = userService;
        this.passwordService = passwordService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public @ResponseBody
    UserBody checkUserCredentials(@RequestParam(value = "email") String email, @RequestParam("pass") String password) {
        return userMapper.mapFromUserBoToUser(loginService.checkIfPasswordAndEmailIsCorrect(email, password));
    }

    @PostMapping
    UserBody changeUserPassword(
            @RequestParam(value = "password") String password,
            @RequestParam(value = "token") String token) {


        passwordService.changeUserPassword(password,token);

        return null;
    }

}
