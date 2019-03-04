package pl.betse.beontime.service;

import org.springframework.stereotype.Service;
import pl.betse.beontime.entity.DepartmentEntity;
import pl.betse.beontime.entity.UserEntity;
import pl.betse.beontime.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UsersService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findById(Integer userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public boolean existsByUserId(Integer userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByEmailLogin(String userEmail) {
        return userRepository.existsByEmailLogin(userEmail);
    }

    @Override
    public UserEntity getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public List<UserEntity> findByDepartmentEntity(DepartmentEntity department) {
        return userRepository.findByDepartmentEntity(department);
    }

    @Override
    public void deleteById(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserEntity findByGUID(String GUID) {
        return userRepository.findByUserGUID(GUID);
    }

    @Override
    public boolean existsByGUID(String GUID) {
        return userRepository.existsByUserGUID(GUID);
    }

    @Override
    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
