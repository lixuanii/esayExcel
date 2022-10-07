package com.lixuan.esayexcel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lixuan.esayexcel.dto.CustomerExportDto;
import com.lixuan.esayexcel.dto.excel.BigDataExcelWriteDto;
import com.lixuan.esayexcel.entity.Customer;
import com.lixuan.esayexcel.enums.OaSystemModuleNameEnum;
import com.lixuan.esayexcel.mapper.CustomerMapper;
import com.lixuan.esayexcel.service.CustomerService;
import com.lixuan.esayexcel.service.excel.CustomerExportServiceImpl;
import com.lixuan.esayexcel.util.EasyExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private EasyExcelUtils easyExcelUtils;

    /**
     * 导出客户信息
     *
     * @param condition 查询条件
     * @author lixuan
     * @date 2022/9/24 22:49
     **/
    @Override
    public void exportExcel(Customer condition) {

        CompletableFuture.runAsync(() -> {
            BigDataExcelWriteDto<CustomerExportDto> dto = new BigDataExcelWriteDto<>();
            LambdaQueryWrapper<Customer> wrapper = this.buildWrapper(condition);
            dto.setCount(super.count(wrapper));
            dto.setExcelWriteService(new CustomerExportServiceImpl(wrapper));
            dto.setMaxSize(500000);
            dto.setSize(1000);
            dto.setClazz(CustomerExportDto.class);
            dto.setFileName("客户列表");
            dto.setSheetName("客户信息");
            dto.setRealName("当前登录人");
            dto.setModel(OaSystemModuleNameEnum.CUSTOMER_LIST);
            easyExcelUtils.exportAndUpload(dto);
        });

    }

    private LambdaQueryWrapper<Customer> buildWrapper(Customer condition) {

        return Wrappers.lambdaQuery(condition);
    }
}
