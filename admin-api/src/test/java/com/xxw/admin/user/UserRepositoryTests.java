package com.xxw.admin.user;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.xxw.admin.MysqlDbUnitTestExecutionListener;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.SQLIntegrityConstraintViolationException;
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
    @ExpectedDatabase(value = "/UserRepositoryTestsSave.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown(value = "/UserRepositoryTestsInit.xml")
    public void save() {
        UserPO userPO = new UserPO()
                .setUsername("zhangsan")
                .setPassword("123456")
                .setRealName("张三")
                .setMobile("18888888888")
                .setEmail("111111111@qq.com")
                .setPosition("管理员")
                .setStatus(1);
        userRepository.save(userPO);
    }

    @Test
    @DatabaseTearDown(value = "/UserRepositoryTestsInit.xml")
    public void ukUsername() {
        UserPO userPO = new UserPO()
                .setUsername("zhangsan")
                .setPassword("123456")
                .setRealName("张三")
                .setMobile("18888888888")
                .setEmail("111111111@qq.com")
                .setPosition("管理员")
                .setStatus(1);
        userRepository.save(userPO);
        userPO.setId(null);
        try {
            userRepository.save(userPO);
        } catch (Exception e) {
            String constraintName = ((ConstraintViolationException) e.getCause()).getConstraintName();
            Assert.assertEquals("uk_username", constraintName);
            return;
        }
        Assert.fail();
    }

//    @Test
//    @DatabaseSetup(value = "/UserRepositoryTests.xml")
//    @ExpectedDatabase(value = "/UserRepositoryTests2.xml")
//    @DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
    public void find() {
        Optional<UserPO> userPO = userRepository.findById(100001);
        if (userPO.isPresent()) {
            System.out.println(userPO.get().getRealName());
            System.out.println(userPO.get().getCreateTime());
        }
        else {
            System.out.println("未找到用户");
        }
    }
}