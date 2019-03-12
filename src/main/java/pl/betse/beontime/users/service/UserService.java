package pl.betse.beontime.users.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.entity.PasswordTokenEntity;
import pl.betse.beontime.users.entity.RoleEntity;
import pl.betse.beontime.users.entity.UserEntity;
import pl.betse.beontime.users.exception.*;
import pl.betse.beontime.users.mapper.UserMapper;
import pl.betse.beontime.users.repository.DepartmentRepository;
import pl.betse.beontime.users.repository.PasswordTokenRepository;
import pl.betse.beontime.users.repository.RoleRepository;
import pl.betse.beontime.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;

    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, PasswordTokenRepository passwordTokenRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
    }

    public boolean existsByEmail(String userEmail) {
        if (!userRepository.existsByEmail(userEmail)) {
            log.error("User with email " + userEmail + " doesn't exist!");
            throw new UserNotFoundException();
        }
        return true;
    }

    public boolean existsByGuid(String GUID) {
        if (!userRepository.existsByGuid(GUID)) {
            log.error("User with ID=" + GUID + " doesn't exist!");
            throw new UserNotFoundException();
        }
        return true;
    }

    public List<UserBo> findAll() {
        List<UserBo> userList = userRepository.findAll().stream()
                .map(userMapper::mapFromUserEntity)
                .collect(Collectors.toList());
        if (userList.isEmpty()) {
            log.error("Empty user list.");
            throw new EmptyUserListException();
        }
        return userList;
    }


    public UserBo findByEmail(String userEmail) {
        existsByEmail(userEmail);
        return userMapper.mapFromUserEntity(userRepository.findByEmail(userEmail));
    }

    public UserBo findByGuid(String GUID) {
        if (!existsByGuid(GUID)) {
            log.error("User doesn't exist.");
            throw new UserNotFoundException();
        }
        return userMapper.mapFromUserEntity(userRepository.findByGuid(GUID));
    }

    public UserBo createUser(UserBo userBo, String originUrl) {
        if (userRepository.existsByEmail(userBo.getEmail())) {
            log.error("User with email {} currently exist in database", userBo.getEmail());
            throw new UserAlreadyExistException();
        }
        if (!departmentRepository.existsByName(userBo.getDepartment())) {
            log.error("Department '" + userBo.getDepartment() + "' doesn't exist.");
            throw new DepartmentNotFoundException();
        }
        checkIfRoleExist(userBo.getRoles());
        UserEntity userEntity = userMapper.mapFromUserBo(userBo);
        capitalizeFirstNameAndLastName(userEntity, userBo);
        userEntity.setGuid(UUID.randomUUID().toString());
        //  SET PASSWORD FOR EVERY NEW USER (HARDCODED FOR NOW!) -> Random password based on random generated UID
        userEntity.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        userEntity.setDepartment(departmentRepository.findByName(userBo.getDepartment()));
        userEntity.setRoles(buildRoleEntityListFromString(userBo.getRoles()));
        userRepository.save(userEntity);

        PasswordTokenEntity passwordTokenEntity = new PasswordTokenEntity();
        passwordTokenEntity.setUserEntity(userEntity);
        passwordTokenEntity.setToken(UUID.randomUUID().toString());
        passwordTokenRepository.save(passwordTokenEntity);
        passwordService.sendMessageToUser(userMapper.mapFromUserEntity(userEntity), originUrl);
        return userMapper.mapFromUserEntity(userEntity);
    }


    public UserBo editAllUserFields(String GUID, UserBo userBo) {
        existsByGuid(GUID);
        // CURRENTLY ONLY NAME, LAST NAME, ACTIVE STATUS AND ROLE CAN BE CHANGED (BUSINESS REQUIREMENTS)
        UserEntity userEntity = userRepository.findByGuid(GUID);
        userEntity.setFirstName(userBo.getFirstName());
        userEntity.setLastName(userBo.getLastName());
        userEntity.setActive(userBo.isActive());
        if (userBo.getRoles() == null) {
            userEntity.setRoles(new ArrayList<>());
        } else {
            editRolesIfDifferent(userBo, userEntity);
        }
        userRepository.save(userEntity);
        return userMapper.mapFromUserEntity(userEntity);
    }

    public UserBo editUserFields(String GUID, UserBo userBo) {
        existsByGuid(GUID);
        // CURRENTLY ONLY NAME, LAST NAME, ACTIVE STATUS AND ROLE CAN BE CHANGED (BUSINESS REQUIREMENTS)
        UserEntity userEntity = userRepository.findByGuid(GUID);
        capitalizeFirstNameAndLastName(userEntity, userBo);
        if (userEntity.isActive() != userBo.isActive()) {
            userEntity.setActive(userBo.isActive());
        }
        editRolesIfDifferent(userBo, userEntity);
        userRepository.save(userEntity);
        return userMapper.mapFromUserEntity(userEntity);
    }

    public void deleteByGuid(String GUID) {
        existsByGuid(GUID);
        userRepository.deleteById(userRepository.findByGuid(GUID).getId());
    }

    private void editRolesIfDifferent(UserBo userBo, UserEntity userEntity) {
        // ROLE LIST CAN BE EMPTY
        if (userBo.getRoles() != null) {
            checkIfRoleExist(userBo.getRoles());
            userEntity.setRoles(buildRoleEntityListFromString(userBo.getRoles()));
        }
    }

    private void checkIfRoleExist(List<String> userRoles) {
        for (String role : userRoles) {
            if (!roleRepository.existsByName(role)) {
                log.error("Role " + role + " doesn't exist.");
                throw new RoleNotFoundException();
            }
        }
    }

    private List<RoleEntity> buildRoleEntityListFromString(List<String> roleStringList) {
        return roleStringList.stream()
                .map(roleRepository::findByName)
                .collect(Collectors.toList());
    }

    private static boolean isNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private String capitalizeStringValue(String toCorrect) {
        return toCorrect.substring(0, 1).toUpperCase() + toCorrect.substring(1).toLowerCase();
    }

    private void capitalizeFirstNameAndLastName(UserEntity userEntity, UserBo userBo) {
        if (isNullOrEmpty(userBo.getFirstName())) {
            userEntity.setFirstName(capitalizeStringValue(userBo.getFirstName()));
        }
        if (isNullOrEmpty(userBo.getLastName())) {
            userEntity.setLastName(capitalizeStringValue(userBo.getLastName()));
        }
    }
}
