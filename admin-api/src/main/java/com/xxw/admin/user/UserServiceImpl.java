package com.xxw.admin.user;

import com.github.dozermapper.core.Mapper;
import com.xxw.admin.user.pojo.UserDTO;
import com.xxw.admin.user.pojo.UserPO;
import com.xxw.common.exception.BusinessException;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/26
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_EXIST = "用户不存在";

    private static final String USERNAME_EXISTED = "用户名已存在";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Mapper mapper;

    @Value("${password.secret-key}")
    private String passwordSecretKey;

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

    public void save(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException(USERNAME_EXISTED);
        }
        userDTO.setPassword(HmacUtils.hmacSha256Hex(passwordSecretKey, userDTO.getPassword()));
        UserPO userPO = mapper.map(userDTO, UserPO.class);
        userRepository.save(userPO);
    }

    public UserDTO findById(Integer id) {
        Optional<UserPO> userPO = userRepository.findById(id);
        if (userPO.isPresent()) {
            return mapper.map(userPO.get(), UserDTO.class);
        }
        else {
            throw new BusinessException(USER_NOT_EXIST);
        }
    }

    public void update(UserDTO userDTO) {
        if (StringUtils.isNotBlank(userDTO.getUsername())
                && userRepository.existsByUsernameAndIdNot(userDTO.getUsername(), userDTO.getId())) {
            throw new BusinessException(USERNAME_EXISTED);
        }
        if (StringUtils.isNotBlank(userDTO.getPassword())) {
            userDTO.setPassword(HmacUtils.hmacSha256Hex(passwordSecretKey, userDTO.getPassword()));
        }
        Optional<UserPO> userPO = userRepository.findById(userDTO.getId());
        if (userPO.isPresent()) {
            mapper.map(userDTO, userPO.get());
            userRepository.save(userPO.get());
        }
        else {
            throw new BusinessException(USER_NOT_EXIST);
        }
    }

    public void deleteById(Integer id) {
        Optional<UserPO> userPO = userRepository.findById(id);
        if (userPO.isPresent()) {
            if (userPO.get().getFixed()) {
                throw new BusinessException("固定用户不能删除");
            }
            else {
                userRepository.deleteById(id);
            }
        }
        else {
            throw new BusinessException(USER_NOT_EXIST);
        }
    }

    public Page<UserDTO> findAll(UserDTO userDTO, Pageable pageable) {
        return userRepository.findAll((Specification<UserPO>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(userDTO.getId())) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), userDTO.getId()));
            }
            if (StringUtils.isNotBlank(userDTO.getUsername())) {
                predicateList.add(criteriaBuilder.like(root.get("username"), "%" + userDTO.getUsername() + "%"));
            }
            if (StringUtils.isNotBlank(userDTO.getNickname())) {
                predicateList.add(criteriaBuilder.like(root.get("nickname"), "%" + userDTO.getNickname() + "%"));
            }
            if (Objects.nonNull(userDTO.getEnabled())) {
                predicateList.add(criteriaBuilder.equal(root.get("enabled"), userDTO.getEnabled()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable).map(userPO -> mapper.map(userPO, UserDTO.class));
    }
}
