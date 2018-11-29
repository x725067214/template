package com.xxw.admin.user;

import com.github.dozermapper.core.Mapper;
import com.xxw.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/26
 */
@Service
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        Optional<UserPO> userPO = userRepository.findByUsername(username);
        if (userPO.isPresent()) {
            return Optional.of(mapper.map(userPO.get(), UserDTO.class));
        }
        else {
            return Optional.empty();
        }
    }

    public UserDTO findById(Integer id) {
        Optional<UserPO> userPO = userRepository.findById(id);
        if (userPO.isPresent()) {
            return mapper.map(userPO.get(), UserDTO.class);
        }
        else {
            throw new BusinessException("用户不存在");
        }
    }
}
