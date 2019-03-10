package pl.betse.beontime.users.controller;

import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.users.mapper.UserMapper;
import pl.betse.beontime.users.model.UserBody;
import pl.betse.beontime.users.service.LoginService;

@RestController
@RequestMapping("/login")
@CrossOrigin("*")
public class LoginController {

    private final LoginService loginService;
    private final UserMapper userMapper;

    public LoginController(LoginService loginService, UserMapper userMapper) {
        this.loginService = loginService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public @ResponseBody
    UserBody checkUserCredentials(@RequestParam(value = "email") String email, @RequestParam("pass") String password) {
        return userMapper.mapFromUserBoToUser(loginService.checkIfPasswordAndEmailIsCorrect(email, password));
    }

}
