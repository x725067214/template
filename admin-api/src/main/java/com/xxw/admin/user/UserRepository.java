package com.xxw.admin.user;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author xxw
 * @date 2018/11/23
 */
public interface UserRepository extends PagingAndSortingRepository<UserPO, Integer> {
}
