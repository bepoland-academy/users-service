package pl.betse.beontime.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.mapper.UserMapper;
import pl.betse.beontime.users.model.UserBody;
import pl.betse.beontime.users.service.UserService;
import pl.betse.beontime.users.validation.CreateUserValidation;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<UserBody>> getAllUsers() {
        List<UserBody> users = userMapper.mapFromUserBosToUsers(userService.findAll());
        users.forEach(this::addLinks);
        return ResponseEntity.ok(users);
    }


    private void addLinks(UserBody userBody) {
        userBody.add(linkTo(methodOn(UserController.class).getUserByGuid(userBody.getUserId())).withSelfRel());
//        if (isAdministration()) {
        userBody.add(linkTo(methodOn(UserController.class).deleteUserById(userBody.getUserId())).withRel("DELETE"));
//        }
    }

//    private boolean isAdministration() {
//        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_ADMINISTRATION");
//    }

    @GetMapping(path = "/{guid}")
    public @ResponseBody
    ResponseEntity<UserBody> getUserByGuid(@PathVariable("guid") String guid) {
        UserBody user = userMapper.mapFromUserBoToUser(userService.findByGuid(guid));
        addLinks(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserBody userBody, HttpServletRequest httpServletRequest) {
        UserBo userBo = userService.createUser(userMapper.mapFromUserToUserBo(userBody));
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
        UserBody userBody = userMapper.mapFromUserBoToUser(userService.editUserFields(userGuid, userMapper.mapFromUserToUserBo(requestUserBody)));
        addLinks(userBody);
        URI location = linkTo(methodOn(UserController.class).getUserByGuid(userBody.getUserId())).toUri();
        return ResponseEntity.ok(userBody);
    }

    @DeleteMapping(path = "/{guid}")
    public ResponseEntity deleteUserById(@PathVariable("guid") String userGUID) {
        userService.deleteById(Long.parseLong(userGUID));
        return ResponseEntity.ok().build();
    }
}
