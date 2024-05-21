package com.rainlf.weixinmpserver.dao;

import com.rainlf.weixinmpserver.User;
import com.rainlf.weixinmpserver.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author rain
 * @date 5/21/2024 7:29 AM
 */
@Slf4j
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        List<User> users = userMapper.selectList(null);
        log.info(users.toString());
    }
}
