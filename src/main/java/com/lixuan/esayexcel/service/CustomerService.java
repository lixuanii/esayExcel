package com.lixuan.esayexcel.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lixuan.esayexcel.entity.Customer;
import com.lixuan.esayexcel.entity.User;


public interface CustomerService extends IService<Customer> {

    /**
     * 导出客户信息
     *
     * @param condition 查询条件
     * @author lixuan
     * @date 2022/9/24 22:49
     **/
    void exportExcel(Customer condition);

}
