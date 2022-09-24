package com.lixuan.esayexcel.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lixuan.esayexcel.dto.UserExportDto;
import com.lixuan.esayexcel.dto.excel.SimpleExcelWriteDto;
import com.lixuan.esayexcel.entity.User;
import com.lixuan.esayexcel.enums.OaSystemModuleNameEnum;
import com.lixuan.esayexcel.mapper.UserMapper;
import com.lixuan.esayexcel.service.UserService;
import com.lixuan.esayexcel.util.EasyExcelUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private EasyExcelUtils easyExcelUtils;

    /**
     * 导出用户
     *
     * @param condition 查询条件
     * @author lixuan
     * @date 2022/9/24 22:05
     **/
    @Override
    public void exportExcel(User condition) {
        val wrapper = buildWrapper(condition);
        List<User> userList = super.list(wrapper);
        if (CollUtil.isEmpty(userList)) {
            log.info("导出失败！暂无匹配数据");
        }
        CompletableFuture.runAsync(() -> {
            SimpleExcelWriteDto<UserExportDto, User> dto = new SimpleExcelWriteDto<>();
            dto.setClazz(UserExportDto.class);
            dto.setData(userList);
            dto.setModel(OaSystemModuleNameEnum.USER_INFO);
            dto.setRealName("当前登录人");
            dto.setFileName("用户数据");
            dto.setSheetName("用户信息");
            easyExcelUtils.exportAndUpload(dto);
        });

    }


    private LambdaQueryWrapper<User> buildWrapper(User condition) {
        // build condition
        return Wrappers.lambdaQuery(User.class);
    }
}
