package com.xxw.admin.user;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.xxw.admin.MysqlDbUnitTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        MysqlDbUnitTestExecutionListener.class})
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DatabaseSetup(value = "/UserRepositoryTests.xml")
    @ExpectedDatabase(value = "/UserRepositoryTests2.xml")
    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void find() {
        Optional<UserPO> userPO = userRepository.findById(100001);
        if (userPO.isPresent()) {
            System.out.println(userPO.get().realName());
            System.out.println(userPO.get().createTime());
        }
        else {
            System.out.println("未找到用户");
        }
    }
}