package com.lixuan.esayexcel.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 用户表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
public class User extends BaseEntity<User> {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 员工编号
     */
    private String code;

    /**
     * 账户名称
     */
    private String username;

    /**
     * 英文名称
     */
    private String englishName;

    /**
     * 密密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机/联系方式
     */
    private String phone;


    /**
     * 用户状态 1:在职 2:离职 3:休假
     */
    private Integer userState;

    /**
     * 账户状态  0:停用  1:启用
     */
    private Integer accountState;

    /**
     * 用户权限类型  1:普通用户 2:个性化  3:未授权用户
     */
    private Integer permissionType;

    /**
     * 身份证
     */
    private String idcard;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 性别 0:男 1:女
     */
    private Integer sex;


    /**
     * 学历 0:小学 1:初中 2:高中 3:大专 4:本科 5: 硕士 6:博士
     */
    private Integer education;

    /**
     * 紧急联系人
     */
    private String emergencyContact;

    /**
     * 紧急联系人电话
     */
    private String emergencyContactPhone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
