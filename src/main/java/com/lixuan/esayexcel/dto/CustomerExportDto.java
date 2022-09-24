package com.lixuan.esayexcel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.lixuan.esayexcel.convert.FinancialStatusConverter;
import lombok.Data;


@Data
public class CustomerExportDto {

    @ExcelProperty(value = "账户名称")
    @ColumnWidth(20)
    private String username;

    @ExcelProperty(value = "客户别名")
    @ColumnWidth(20)
    private String customerAlias;

    @ExcelProperty(value = "公司名称")
    @ColumnWidth(20)
    private String company;

    @ExcelProperty(value = "财务状态", converter = FinancialStatusConverter.class)
    @ColumnWidth(20)
    private String accountStatus;

}
