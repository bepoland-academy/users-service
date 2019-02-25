package pl.betse.beontime.controller;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.bo.UserDTO;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.entity.RoleEntity;
import pl.betse.beontime.model.enums.DepartmentEnum;
import pl.betse.beontime.model.enums.RoleEnum;
import pl.betse.beontime.model.validation.CreateUserValidation;
import pl.betse.beontime.service.DepartmentService;
import pl.betse.beontime.service.RoleService;
import pl.betse.beontime.service.UsersService;
import pl.betse.beontime.utils.CustomResponseMessage;
import pl.betse.beontime.utils.DTOResponseConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UsersService usersService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public @ResponseBody
    ResponseEntity<?> obtainAllUsers() {
        List<UserDTO> userList = new ArrayList<>();
        usersService.findAll()
                .forEach(x ->
                        DTOResponseConstructor.buildUserDTOListWithRoles(userList, x));
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    ResponseEntity<?> getUserById(@PathVariable("id") String userId) {
        if (!usersService.existsByUserId(Integer.valueOf(userId))) {
            return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "UserEntity with ID=" + userId + " doesn't exist!"), HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = usersService.findById(Integer.valueOf(userId));

        Set<String> userRoles = new HashSet<>();

        for (RoleEntity roleEntity : userEntity.getRoles()) {
            userRoles.add(new StringBuilder().append(roleEntity.getRole()).toString());
        }

        UserDTO userDTO = UserDTO.builder()
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .emailLogin(userEntity.getEmailLogin())
                .isActive(userEntity.isActive())
                .department(userEntity.getDepartmentEntity().getName())
                .roles(userRoles)
                .build();


        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    @PostMapping
    public @ResponseBody
    CustomResponseMessage createNewUser(@RequestBody @Validated(CreateUserValidation.class) UserDTO userDTO) {
        if (!EnumUtils.isValidEnum(DepartmentEnum.class, userDTO.getDepartment().toUpperCase())) {
            return new CustomResponseMessage(HttpStatus.BAD_REQUEST, "Department does't exist!");
        }

        Set<RoleEntity> newRoleEntities = new HashSet<>();
        if (userDTO.getRoles() != null) {
            if (validateUserRoles(userDTO, newRoleEntities))
                return new CustomResponseMessage(HttpStatus.BAD_REQUEST, "Role doesn't exist!");
        }
        UserEntity newUserEntity = UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .isActive(userDTO.isActive())
                .emailLogin(userDTO.getEmailLogin())
                .password(passwordEncoder.encode("qwe123!"))
                .departmentEntity(departmentService.findByName(userDTO.getDepartment()))
                .roles(newRoleEntities)
                .build();

        usersService.save(newUserEntity);
        return new CustomResponseMessage(HttpStatus.CREATED, newUserEntity.toString());
    }


    @PutMapping(path = "/{id}")
    public @ResponseBody
    CustomResponseMessage updateUser(@PathVariable("id") String userId, @RequestBody UserDTO userDTO) {

        if (!usersService.existsByUserId(Integer.valueOf(userId))) {
            return new CustomResponseMessage(HttpStatus.BAD_REQUEST, "UserEntity with ID=" + userId + " doesn't exist!");
        }

        UserEntity userEntity = usersService.findById(Integer.valueOf(userId));

        if (userDTO.getDepartment() != null) {
            if (!EnumUtils.isValidEnum(DepartmentEnum.class, userDTO.getDepartment().toUpperCase())) {
                return new CustomResponseMessage(HttpStatus.BAD_REQUEST, "Department does't exist!");
            }

            userEntity.setDepartmentEntity(departmentService.findByName(userDTO.getDepartment()));
        }

        Set<RoleEntity> newRoleEntities = new HashSet<>();

        if (userDTO.getRoles() != null) {
            if (validateUserRoles(userDTO, newRoleEntities))
                return new CustomResponseMessage(HttpStatus.BAD_REQUEST, "Role doesn't exist!");
            userEntity.setRoles(newRoleEntities);
        }
        if (userDTO.getEmailLogin() != null) {
            if (usersService.existsByEmailLogin(userDTO.getEmailLogin())) {
                return new CustomResponseMessage(HttpStatus.BAD_REQUEST, "UserEntity with email: " + userDTO.getEmailLogin() + " currently exists in database!");
            }
            userEntity.setEmailLogin(userDTO.getEmailLogin());
        }
        if (userDTO.getFirstName() != null) {
            userEntity.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            userEntity.setLastName(userDTO.getLastName());
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

        return new CustomResponseMessage(HttpStatus.CREATED, "UserEntity successfully updated.");
    }


    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    CustomResponseMessage deleteUserById(@PathVariable("id") String userId) {

        if (!usersService.existsByUserId(Integer.valueOf(userId))) {
            return new CustomResponseMessage(HttpStatus.BAD_REQUEST, "UserEntity with ID=" + userId + " doesn't exist!");
        }

        usersService.deleteById(Integer.valueOf(userId));

        return new CustomResponseMessage(HttpStatus.OK, "User succesfully deleted!");
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
