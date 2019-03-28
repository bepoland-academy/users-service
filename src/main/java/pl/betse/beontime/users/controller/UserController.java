package pl.betse.beontime.users.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    private static final String SCHEMA_SEPARATOR = "://";
    private static final String BEONTIME_DOMAIN = "beontime.be-academy.pl";

    @Value("${api-prefix}")
    private String API_PREFIX;

    private final UserService userService;
    private final UserMapper userMapper;


    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<Resources<UserBody>> getAllUsers(@RequestParam(name = "department", required = false) String department) {
        List<UserBody> users;
        if (department != null && !department.isEmpty()) {
            users = userService.findByDepartment(department).stream()
                    .map(userMapper::fromBoToBody)
                    .collect(Collectors.toList());
        } else {
            users = userService.findAll().stream()
                    .map(userMapper::fromBoToBody)
                    .collect(Collectors.toList());
        }
        users.forEach(this::addLinks);
        return ResponseEntity.ok(new Resources<>(users));
    }

    @GetMapping(path = "/{guid}")
    public ResponseEntity<Resource<UserBody>> getUserByGuid(@PathVariable("guid") String guid) {
        UserBody user = userMapper.fromBoToBody(userService.findByGuid(guid));
        addLinks(user);
        return ResponseEntity.ok(new Resource<>(user));
    }

    @PostMapping
    public ResponseEntity createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserBody userBody, HttpServletRequest httpServletRequest) throws URISyntaxException {
        String originLink = buildOriginRequestUrl(httpServletRequest);
        UserBo userBo = userService.createUser(userMapper.mapFromUserToUserBo(userBody), originLink);
        URI location = linkTo(methodOn(UserController.class).getUserByGuid(userBo.getUserId())).toUri();
        return ResponseEntity.created(new URI(API_PREFIX+location.getPath())).build();
    }

    @PutMapping(path = "/{guid}")
    public ResponseEntity updateAllUserFields(@PathVariable("guid") String userGuid, @RequestBody UserBody requestUserBody) {
        userService.editAllUserFields(userGuid, userMapper.mapFromUserToUserBo(requestUserBody));
        return ResponseEntity.ok().build();
    }

    private void addLinks(UserBody userBody) {
        Link link = constructLink(userBody.getUserId());
        userBody.add(link);
    }

    private String buildOriginRequestUrl(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getScheme() + SCHEMA_SEPARATOR + BEONTIME_DOMAIN;
    }

    private Link constructLink(String userGuid) {
        URI location = linkTo(methodOn(UserController.class).getUserByGuid(userGuid)).toUri();
        return new Link(API_PREFIX + location.getPath()).withSelfRel();
    }
}
