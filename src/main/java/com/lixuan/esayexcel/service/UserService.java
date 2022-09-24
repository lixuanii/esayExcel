package com.lixuan.esayexcel.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lixuan.esayexcel.entity.User;


public interface UserService extends IService<User> {


    /**
     * 导出用户
     *
     * @param condition 查询条件
     * @author lixuan
     * @date 2022/9/24 22:04
     **/
    void exportExcel(User condition);

}
