package com.lixuan.esayexcel.service;

import com.lixuan.esayexcel.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerServiceTest {


    @Autowired
    private CustomerService customerService;

    @Test
    void exportExcel() {
        Customer condition = new Customer();
        condition.setAccountStatus(0);

        customerService.exportExcel(condition);
    }

}