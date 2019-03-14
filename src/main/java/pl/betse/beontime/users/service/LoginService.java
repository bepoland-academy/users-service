package pl.betse.beontime.users.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.entity.UserEntity;
import pl.betse.beontime.users.exception.UserBadCredentialException;
import pl.betse.beontime.users.mapper.UserMapper;
import pl.betse.beontime.users.repository.UserRepository;

@Slf4j
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserBo checkIfPasswordAndEmailIsCorrect(String email, String password) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(UserBadCredentialException::new);
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            log.error("Incorrect password.");
            throw new UserBadCredentialException();
        }
        return userMapper.fromEntityToBo(userEntity);
    }
}
