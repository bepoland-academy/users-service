package pl.betse.beontime.controller;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
import pl.betse.beontime.utils.GUIDGenerator;
import pl.betse.beontime.utils.UpperAndLoweCaseCorrector;
import pl.betse.beontime.utils.UserDTOListBuilder;

import javax.websocket.server.PathParam;
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
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @Secured({"ROLE_MANAGER", "ROLE_ADMINISTRATION"})
    public @ResponseBody
    List<UserDTO> obtainAllUsers() {
        List<UserDTO> userList = usersService.findAll().stream()
                .map(UserModelMapper::fromUserEntityToUserDTO)
                .collect(Collectors.toList());

//        usersService.findAll()
//                .forEach(x ->
//                        UserDTOListBuilder.build(userList, x));


        if (userList.isEmpty()) {
            throw new EmptyUserListException();
        }

        return userList;
    }

    @GetMapping(path = "/{guid}")
    public @ResponseBody
    UserDTO getUserById(@PathVariable("guid") String userGUID) {

        if (!usersService.existsByGUID(userGUID)) {
            throw new UserNotFoundException();
        }

        return UserModelMapper.fromUserEntityToUserDTO(usersService.findByGUID(userGUID));
    }

    @GetMapping(path = "/email")
    public @ResponseBody
    UserDTO getUserByEmail(@RequestParam("value") String email) {
        if (!usersService.existsByEmailLogin(email)) {
            throw new UserNotFoundException();
        }
        return UserModelMapper.fromUserEntityToUserDTO(usersService.getUserByEmail(email));
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

        try {
            userDTO.setFirstName(UpperAndLoweCaseCorrector.fix(userDTO.getFirstName()));
            userDTO.setLastName(UpperAndLoweCaseCorrector.fix(userDTO.getLastName()));
        } catch (Exception e) {
        }

        userDTO.setUserGUID(GUIDGenerator.generate());

        System.out.println(userDTO.getUserGUID());

        UserEntity newUserEntity = UserModelMapper.fromUserDtoToUserEntity(userDTO);
        // SET PASSWORD FOR EVERY NEW USER (HARDCODED FOR NOW!)
        newUserEntity.setPassword(passwordEncoder.encode("qwe123!"));
        newUserEntity.setDepartmentEntity(departmentService.findByName(userDTO.getDepartment()));
        newUserEntity.setRoles(newRoleEntities);

        usersService.save(newUserEntity);

        return UserModelMapper.fromUserEntityToUserDTO(newUserEntity);
    }


    @PutMapping(path = "/{guid}")
    public @ResponseBody
    UserDTO updateUser(@PathVariable("guid") String userGuid, @RequestBody UserDTO userDTO) {

        if (!usersService.existsByGUID(userGuid)) {
            throw new UserNotFoundException();
        }

        UserEntity userEntity = usersService.findByGUID(userGuid);

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


    @DeleteMapping(path = "/{guid}")
    public @ResponseBody
    CustomResponseMessage deleteUserById(@PathVariable("guid") String userGUID) {

        if (!usersService.existsByGUID(userGUID)) {
            throw new UserNotFoundException();
        }

        usersService.deleteById(usersService.findByGUID(userGUID).getUserId());

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
