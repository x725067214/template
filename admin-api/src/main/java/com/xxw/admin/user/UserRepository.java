package com.xxw.admin.user;

import com.xxw.admin.user.pojo.UserPO;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/23
 */
public interface UserRepository extends CrudRepository<UserPO, Integer>, JpaSpecificationExecutor<UserPO> {

    Optional<UserPO> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Integer id);
}
