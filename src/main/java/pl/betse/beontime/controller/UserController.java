package pl.betse.beontime.controller;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.model.custom_exceptions.*;
import pl.betse.beontime.model.enums.DepartmentEnum;
import pl.betse.beontime.model.enums.RoleEnum;
import pl.betse.beontime.model.validation.CreateUserValidation;
import pl.betse.beontime.model_mapper.UserModelMapper;
import pl.betse.beontime.service.DepartmentService;
import pl.betse.beontime.service.RoleService;
import pl.betse.beontime.service.UsersService;
import pl.betse.beontime.utils.CustomResponseMessage;
import pl.betse.beontime.utils.UpperAndLoweCaseCorrector;
import pl.betse.beontime.utils.UserDTOListBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    List<UserDTO> obtainAllUsers() {
        List<UserDTO> userList = new ArrayList<>();
        usersService.findAll()
                .forEach(x ->
                        UserDTOListBuilder.build(userList, x));


        if (userList.isEmpty()) {
            throw new EmptyUserListException();
        }

        return userList;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    UserDTO getUserById(@PathVariable("id") String userId) {

        if (!usersService.existsByUserId(Integer.valueOf(userId))) {
            throw new UserNotFoundException();
        }

        return UserModelMapper.fromUserEntityToUserDTO(usersService.findById(Integer.valueOf(userId)));
    }


    @PostMapping
    public @ResponseBody
    UserDTO createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserDTO userDTO) {

        if (usersService.existsByEmailLogin(userDTO.getEmailLogin())) {
            throw new UserExistException();
        }

        if (!EnumUtils.isValidEnum(DepartmentEnum.class, userDTO.getDepartment().toUpperCase())) {
            throw new DepartmentNotFoundException();
        }

        Set<RoleEntity> newRoleEntities = new HashSet<>();
        if (userDTO.getRoles() != null) {
            if (validateUserRoles(userDTO, newRoleEntities))
                throw new RoleNotFoundException();
        }

        UserEntity newUserEntity = UserModelMapper.fromUserDtoToUserEntity(userDTO);
        // SET PASSWORD FOR EVERY NEW USER (HARDCODED FOR NOW!)
        newUserEntity.setPassword(passwordEncoder.encode("qwe123!"));
        newUserEntity.setDepartmentEntity(departmentService.findByName(userDTO.getDepartment()));
        newUserEntity.setRoles(newRoleEntities);

        // PERSIST
        usersService.save(newUserEntity);

        return UserModelMapper.fromUserEntityToUserDTO(newUserEntity);
    }


    @PutMapping(path = "/{id}")
    public @ResponseBody
    UserDTO updateUser(@PathVariable("id") String userId, @RequestBody UserDTO userDTO) {

        if (!usersService.existsByUserId(Integer.valueOf(userId))) {
            throw new UserNotFoundException();
        }

        UserEntity userEntity = usersService.findById(Integer.valueOf(userId));

        if (userDTO.getDepartment() != null) {
            if (!EnumUtils.isValidEnum(DepartmentEnum.class, userDTO.getDepartment().toUpperCase())) {
                throw new DepartmentNotFoundException();
            }
            userEntity.setDepartmentEntity(departmentService.findByName(userDTO.getDepartment()));
        }

        Set<RoleEntity> newRoleEntities = new HashSet<>();

        if (userDTO.getRoles() != null) {
            if (validateUserRoles(userDTO, newRoleEntities))
                throw new RoleNotFoundException();
            userEntity.setRoles(newRoleEntities);
        }
        if (userDTO.getEmailLogin() != null) {
            userEntity.setEmailLogin(userDTO.getEmailLogin());
        }
        if (userDTO.getFirstName() != null) {
            userEntity.setFirstName(UpperAndLoweCaseCorrector.fix(userDTO.getFirstName()));
        }
        if (userDTO.getLastName() != null) {
            userEntity.setLastName(UpperAndLoweCaseCorrector.fix(userDTO.getLastName()));
        }
        if (userDTO.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if (userDTO.isActive()) {
            userEntity.setActive(true);
        }

        if (!userDTO.isActive()) {
            userEntity.setActive(false);
        }

        usersService.save(userEntity);

        return UserModelMapper.fromUserEntityToUserDTO(userEntity);
    }


    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    CustomResponseMessage deleteUserById(@PathVariable("id") String userId) {

        if (!usersService.existsByUserId(Integer.valueOf(userId))) {
            throw new UserNotFoundException();
        }

        usersService.deleteById(Integer.valueOf(userId));

        return new CustomResponseMessage(HttpStatus.OK, "User successfully deleted!");
    }

    private boolean validateUserRoles(@Validated(CreateUserValidation.class) @RequestBody UserDTO userDTO, Set<RoleEntity> newRoleEntities) {
        for (String role : userDTO.getRoles()) {
            if (!EnumUtils.isValidEnum(RoleEnum.class, role.toUpperCase())) {
                return true;
            } else {
                newRoleEntities.add(roleService.findByName(role));
            }
        }
        return false;
    }
}
