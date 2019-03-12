package pl.betse.beontime.users.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.entity.DepartmentEntity;
import pl.betse.beontime.users.entity.PasswordTokenEntity;
import pl.betse.beontime.users.entity.RoleEntity;
import pl.betse.beontime.users.entity.UserEntity;
import pl.betse.beontime.users.exception.DepartmentNotFoundException;
import pl.betse.beontime.users.exception.RoleNotFoundException;
import pl.betse.beontime.users.exception.UserAlreadyExistException;
import pl.betse.beontime.users.exception.UserNotFoundException;
import pl.betse.beontime.users.mapper.UserMapper;
import pl.betse.beontime.users.repository.DepartmentRepository;
import pl.betse.beontime.users.repository.PasswordTokenRepository;
import pl.betse.beontime.users.repository.RoleRepository;
import pl.betse.beontime.users.repository.UserRepository;

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

    public List<UserBo> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::fromEntityToBo)
                .collect(Collectors.toList());
    }

    public UserBo findByGuid(String guid) {
        UserEntity userEntity = userRepository.findByGuid(guid)
                .orElseThrow(() -> new UserNotFoundException(guid));
        return userMapper.fromEntityToBo(userEntity);
    }

    public UserBo createUser(UserBo userBo, String originUrl) {
        if (userRepository.existsByEmail(userBo.getEmail())) {
            log.error("User with email {} currently exist in database", userBo.getEmail());
            throw new UserAlreadyExistException();
        }
        DepartmentEntity departmentEntity = departmentRepository.findByName(userBo.getDepartment())
                .orElseThrow(() -> new DepartmentNotFoundException(userBo.getDepartment()));
        UserEntity userEntity = userMapper.fromBoToEntity(userBo);
        capitalizeFirstNameAndLastName(userEntity, userBo);

        //  SET PASSWORD FOR EVERY NEW USER, USER WILL CHANGE IT BY SENT EMAIL
        userEntity.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        userEntity.setDepartment(departmentEntity);
        userEntity.setRoles(buildRoleEntityListFromString(userBo.getRoles()));
        userRepository.save(userEntity);

        PasswordTokenEntity passwordTokenEntity = new PasswordTokenEntity();
        passwordTokenEntity.setUserEntity(userEntity);
        passwordTokenEntity.setToken(UUID.randomUUID().toString());
        passwordTokenRepository.save(passwordTokenEntity);
        passwordService.sendMessageToUser(userMapper.fromEntityToBo(userEntity), originUrl);
        return userMapper.fromEntityToBo(userEntity);
    }


    public void editAllUserFields(String guid, UserBo userBo) {
        UserEntity userEntity = userRepository.findByGuid(guid)
                .orElseThrow(() -> new UserNotFoundException(guid));
        userEntity.setFirstName(userBo.getFirstName());
        userEntity.setLastName(userBo.getLastName());
        userEntity.setActive(userBo.isActive());
        if (userBo.getRoles() != null) {
            editRolesIfDifferent(userBo, userEntity);
        }
        userRepository.save(userEntity);
    }

    private void editRolesIfDifferent(UserBo userBo, UserEntity userEntity) {
        if (userBo.getRoles() != null) {
            userEntity.setRoles(buildRoleEntityListFromString(userBo.getRoles()));
        }
    }


    private List<RoleEntity> buildRoleEntityListFromString(List<String> roleStringList) {
        return roleStringList.stream()
                .map(role -> roleRepository.findByName(role).orElseThrow(RoleNotFoundException::new))
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
