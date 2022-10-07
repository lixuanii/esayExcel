package com.lixuan.esayexcel.service.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lixuan.esayexcel.dto.CustomerExportDto;
import com.lixuan.esayexcel.entity.Customer;
import com.lixuan.esayexcel.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户导出实现
 *
 * @author lixuan
 * @date 2022-09-24 22:33
 */
public class CustomerExportServiceImpl implements ExcelWriteService<CustomerExportDto> {

    private final LambdaQueryWrapper<Customer> wrapper;

    @Override
    public List<CustomerExportDto> getPageList(int current, long size) {
        Page<Customer> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        CustomerService customerService = SpringUtil.getBean(CustomerService.class);
        Page<Customer> customerPage = customerService.page(page, wrapper);
        if (customerPage != null && CollUtil.isNotEmpty(customerPage.getRecords())) {
            return BeanUtil.copyToList(customerPage.getRecords(), CustomerExportDto.class);
        }
        return new ArrayList<>();
    }

    public CustomerExportServiceImpl(LambdaQueryWrapper<Customer> wrapper) {
        this.wrapper = wrapper;
    }
}
