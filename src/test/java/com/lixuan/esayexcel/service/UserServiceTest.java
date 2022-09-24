package com.lixuan.esayexcel.service;


import com.lixuan.esayexcel.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void exportExcel() {
        User condition = new User();
        condition.setAccountState(1);

        userService.exportExcel(condition);
    }

}