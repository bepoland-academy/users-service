package pl.betse.beontime.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.bo.UserBo;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.mapper.UserModelMapper;
import pl.betse.beontime.model.exception.EmptyUserListException;
import pl.betse.beontime.model.exception.UserExistException;
import pl.betse.beontime.model.exception.UserNotFoundException;
import pl.betse.beontime.model.validation.CreateUserValidation;
import pl.betse.beontime.service.DepartmentService;
import pl.betse.beontime.service.RoleService;
import pl.betse.beontime.service.UsersService;
import pl.betse.beontime.utils.ErrorResponse;
import pl.betse.beontime.utils.UpperAndLoweCaseCorrector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class UserController {

    private UsersService usersService;
    private DepartmentService departmentService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    public UserController(UsersService usersService, DepartmentService departmentService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public @ResponseBody
    List<UserBo> obtainAllUsers() {
        List<UserBo> userList = usersService.findAll().stream()
                .map(UserModelMapper::fromUserEntityToUserDTO)
                .collect(Collectors.toList());
        if (userList.isEmpty()) {
            throw new EmptyUserListException();
        }
        return userList;
    }

    @GetMapping(path = "/{guid}")
    public @ResponseBody
    UserBo getUserById(@PathVariable("guid") String userGUID) {

        if (!usersService.existsByGuid(userGUID)) {
            throw new UserNotFoundException();
        }

        return UserModelMapper.fromUserEntityToUserDTO(usersService.findByGuid(userGUID));
    }

    @GetMapping(path = "/email")
    public @ResponseBody
    UserBo getUserByEmail(@RequestParam("value") String email) {
        if (!usersService.existsByEmail(email)) {
            throw new UserNotFoundException();
        }
        return UserModelMapper.fromUserEntityToUserDTO(usersService.getUserByEmail(email));
    }


    @PostMapping
    public @ResponseBody
    UserBo createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserBo userBO) {

        if (usersService.existsByEmail(userBO.getEmail())) {
            throw new UserExistException();
        }

//        if (!EnumUtils.isValidEnum(DepartmentEnum.class, userBO.getDepartment().toUpperCase())) {
//            throw new DepartmentNotFoundException();
//        }

        List<RoleEntity> newRoleEntities = new ArrayList<>();
//        if (userBO.getRoles() != null) {
//            if (validateUserRoles(userBO, newRoleEntities))
//                throw new RoleNotFoundException();
//        }

        try {
            userBO.setFirstName(UpperAndLoweCaseCorrector.fix(userBO.getFirstName()));
            userBO.setLastName(UpperAndLoweCaseCorrector.fix(userBO.getLastName()));
        } catch (Exception e) {
        }

//        userBO.setUserId(GUIDGenerator.generate());
//
//        System.out.println(userBO.getUserId());

        UserEntity newUserEntity = UserModelMapper.fromUserDtoToUserEntity(userBO);
        // SET PASSWORD FOR EVERY NEW USER (HARDCODED FOR NOW!)
        newUserEntity.setPassword(passwordEncoder.encode("qwe123!"));
        newUserEntity.setDepartment(departmentService.findByName(userBO.getDepartment()));
        newUserEntity.setRoles(newRoleEntities);

        usersService.save(newUserEntity);

        return UserModelMapper.fromUserEntityToUserDTO(newUserEntity);
    }


    @PutMapping(path = "/{guid}")
    public @ResponseBody
    UserBo updateUser(@PathVariable("guid") String userGuid, @RequestBody UserBo userBO) {

        if (!usersService.existsByGuid(userGuid)) {
            throw new UserNotFoundException();
        }

        UserEntity userEntity = usersService.findByGuid(userGuid);

//        if (userBO.getDepartment() != null) {
//            if (!EnumUtils.isValidEnum(DepartmentEnum.class, userBO.getDepartment().toUpperCase())) {
//                throw new DepartmentNotFoundException();
//            }
//            userEntity.setDepartment(departmentService.findByName(userBO.getDepartment()));
//        }

        Set<RoleEntity> newRoleEntities = new HashSet<>();

//        if (userBO.getRoles() != null) {
//            if (validateUserRoles(userBO, newRoleEntities))
//                throw new RoleNotFoundException();
//            userEntity.setRoles(newRoleEntities);
//        }
        if (userBO.getEmail() != null) {
            userEntity.setEmail(userBO.getEmail());
        }
        if (userBO.getFirstName() != null) {
            userEntity.setFirstName(UpperAndLoweCaseCorrector.fix(userBO.getFirstName()));
        }
        if (userBO.getLastName() != null) {
            userEntity.setLastName(UpperAndLoweCaseCorrector.fix(userBO.getLastName()));
        }
//        if (userBO.getPassword() != null) {
//            userEntity.setPassword(passwordEncoder.encode(userBO.getPassword()));
//        }

        if (userBO.isActive()) {
            userEntity.setActive(true);
        }

        if (!userBO.isActive()) {
            userEntity.setActive(false);
        }

        usersService.save(userEntity);

        return UserModelMapper.fromUserEntityToUserDTO(userEntity);
    }


    @DeleteMapping(path = "/{guid}")
    public @ResponseBody

//only OK 200
    ResponseEntity<ErrorResponse> deleteUserById(@PathVariable("guid") String userGUID) {
        if (!usersService.existsByGuid(userGUID)) {
            throw new UserNotFoundException();
        }
        //usersService.deleteById(usersService.findByGuid(userGUID).getId());
        return new ResponseEntity<>(new ErrorResponse("User successfully deleted!"), HttpStatus.OK);
    }
}
