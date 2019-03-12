package pl.betse.beontime.users.controller;

import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.users.mapper.UserMapper;
import pl.betse.beontime.users.model.UserBody;
import pl.betse.beontime.users.service.LoginService;
import pl.betse.beontime.users.service.PasswordService;

@RestController
@RequestMapping("/login")
@CrossOrigin("*")
public class LoginController {

    private final LoginService loginService;
    private final PasswordService passwordService;
    private final UserMapper userMapper;

    public LoginController(LoginService loginService, PasswordService passwordService, UserMapper userMapper) {
        this.loginService = loginService;
        this.passwordService = passwordService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public @ResponseBody
    UserBody checkUserCredentials(@RequestParam(value = "email") String email, @RequestParam("pass") String password) {
        return userMapper.fromBoToBody(loginService.checkIfPasswordAndEmailIsCorrect(email, password));
    }

    @PostMapping
    UserBody changeUserPassword(
            @RequestParam(value = "password") String password,
            @RequestParam(value = "token") String token) {


        passwordService.changeUserPassword(password,token);

        return null;
    }

}
