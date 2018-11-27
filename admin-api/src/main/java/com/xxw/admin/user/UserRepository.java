package com.xxw.admin.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/23
 */
public interface UserRepository extends PagingAndSortingRepository<UserPO, Integer> {

    Optional<UserPO> findByUsername(String username);
}
