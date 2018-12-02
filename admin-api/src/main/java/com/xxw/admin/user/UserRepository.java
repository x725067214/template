package com.xxw.admin.user;

import com.xxw.admin.user.pojo.UserPO;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/23
 */
public interface UserRepository extends PagingAndSortingRepository<UserPO, Integer> {

    Optional<UserPO> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Integer id);
}
