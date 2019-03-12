package pl.betse.beontime.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.entity.PasswordTokenEntity;
import pl.betse.beontime.users.mapper.UserMapper;
import pl.betse.beontime.users.model.UserBody;
import pl.betse.beontime.users.repository.PasswordTokenRepository;
import pl.betse.beontime.users.service.PasswordService;
import pl.betse.beontime.users.service.UserService;
import pl.betse.beontime.users.validation.CreateUserValidation;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordService passwordService;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    public UserController(UserService userService, PasswordService passwordService, UserMapper userMapper) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public ResponseEntity<Resources<UserBody>> getAllUsers() {
        List<UserBody> users = userMapper.mapFromUserBosToUsers(userService.findAll());
        users.forEach(this::addLinks);
        Resources<UserBody> userBodyResources = new Resources<>(users);
        userBodyResources.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return ResponseEntity.ok(userBodyResources);
    }

    @GetMapping(path = "/{guid}")
    public ResponseEntity<Resource<UserBody>> getUserByGuid(@PathVariable("guid") String guid) {
        UserBody user = userMapper.mapFromUserBoToUser(userService.findByGuid(guid));
        addLinks(user);
        return ResponseEntity.ok(new Resource<>(user));
    }

    @PostMapping
    public ResponseEntity createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserBody userBody, HttpServletRequest httpServletRequest) {
        String originLink = httpServletRequest.getHeader("x-forwarded-proto") + "://" + httpServletRequest.getHeader("x-forwarded-host");
        UserBo userBo = userService.createUser(userMapper.mapFromUserToUserBo(userBody), originLink);
        URI location = linkTo(methodOn(UserController.class).getUserByGuid(userBo.getUserId())).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "/{guid}")
    public ResponseEntity updateAllUserFields(@PathVariable("guid") String userGuid, @RequestBody UserBody requestUserBody) {
        userMapper.mapFromUserBoToUser(userService.editAllUserFields(userGuid, userMapper.mapFromUserToUserBo(requestUserBody)));
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/{guid}")
    public ResponseEntity<UserBody> updateUserField(@PathVariable("guid") String userGuid, @RequestBody UserBody requestUserBody) {
        userMapper.mapFromUserBoToUser(userService.editUserFields(userGuid, userMapper.mapFromUserToUserBo(requestUserBody)));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{guid}")
    public ResponseEntity deleteUserById(@PathVariable("guid") String userGUID) {
        userService.deleteByGuid(userGUID);
        return ResponseEntity.ok().build();
    }

    private void addLinks(UserBody userBody) {
        userBody.add(linkTo(methodOn(UserController.class).getUserByGuid(userBody.getUserId())).withSelfRel());
        userBody.add(linkTo(methodOn(UserController.class).deleteUserById(userBody.getUserId())).withRel("DELETE"));
    }
}
