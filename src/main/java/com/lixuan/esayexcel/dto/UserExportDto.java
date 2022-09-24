package com.lixuan.esayexcel.dto;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.lixuan.esayexcel.convert.EducationConverter;
import com.lixuan.esayexcel.convert.EnableDisabledStatusConverter;
import com.lixuan.esayexcel.convert.PermissionTypeConverter;
import com.lixuan.esayexcel.convert.UserStateConverter;
import com.lixuan.esayexcel.convert.YesOrNoConverter;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户导出dto
 *
 * @author lixuan
 * @date 2022-09-24 22:09
 */
@Data
public class UserExportDto {


    /**
     * 账户名称
     */
    @ExcelProperty(value = "员工编号")
    private String username;

    /**
     * 英文名称
     */
    @ExcelProperty(value = "英文名称")
    private String englishName;

    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名")
    private String realName;

    /**
     * 手机/联系方式
     */
    @ExcelProperty(value = "手机/联系方式")
    private String phone;

    /**
     * 用户状态 1:在职 2:离职 3:休假
     */
    @ExcelProperty(value = "用户状态", converter = UserStateConverter.class)
    private Integer userState;

    /**
     * 账户状态  0:停用  1:启用
     */
    @ExcelProperty(value = "员工编号", converter = EnableDisabledStatusConverter.class)
    private Integer accountState;

    /**
     * 用户权限类型  1:普通用户 2:个性化  3:未授权用户
     */
    @ExcelProperty(value = "用户权限类型", converter = PermissionTypeConverter.class)
    private Integer permissionType;

    /**
     * 生日
     */
    @DateTimeFormat(DatePattern.NORM_DATETIME_PATTERN)
    @ExcelProperty(value = "生日")
    private LocalDateTime birthday;

    /**
     * 员工编号
     */
    @ExcelProperty(value = "员工编号", converter = YesOrNoConverter.class)
    private Integer sex;

    /**
     * 学历 0:小学 1:初中 2:高中 3:大专 4:本科 5: 硕士 6:博士
     */
    @ExcelProperty(value = "学历", converter = EducationConverter.class)
    private Integer education;

    /**
     * 紧急联系人
     */
    @ExcelProperty(value = "紧急联系人")
    private String emergencyContact;
}
