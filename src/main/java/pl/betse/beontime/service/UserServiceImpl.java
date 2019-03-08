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
    public boolean existsById(Integer userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean existsByEmail(String userEmail) {
        return userRepository.existsByEmail(userEmail);
    }

    @Override
    public UserEntity getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public List<UserEntity> findByDepartment(DepartmentEntity department) {
        return userRepository.findByDepartment(department);
    }

    @Override
    public void deleteById(Integer userId) {
        userRepository.deleteById(userId);
    }


    @Override
    public UserEntity findByGuid(String GUID) {
        return userRepository.findByGuid(GUID);
    }

    @Override
    public boolean existsByGuid(String GUID) {
        return userRepository.existsByGuid(GUID);
    }

    @Override
    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
