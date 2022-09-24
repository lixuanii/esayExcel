package com.lixuan.esayexcel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户
 */
@EqualsAndHashCode(callSuper = true)
@TableName("c_customer")
@Data
public class Customer extends BaseEntity<Customer> {

    /**
     * 客户编号
     */
    private String code;

    /**
     * 账户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 客户别名
     */
    private String customerAlias;

    /**
     * 上级id(客户公司的上下级关系)
     */
    private Integer parentId;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 客户来源，corporation:企业，person:个人
     */
    private String source;

    /**
     * 公司(个人)地址
     */
    private String address;

    /**
     * 营业执照或身份证号
     */
    private String identityCard;

    /**
     * 证件照附件
     */
    private String identityCardAttachment;

    /**
     * 企业负责人名字
     */
    private String coPrincipalName;

    /**
     * 企业负责人电话
     */
    private String coPrincipalTel;

    /**
     * 企业负责人手机
     */
    private String coPrincipalPhone;

    /**
     * 企业负责人qq
     */
    private String coPrincipalQq;

    /**
     * 企业负责人邮箱
     */
    private String coPrincipalEmail;

    /**
     * 物流联系人名字
     */
    private String logisticsContactsName;

    /**
     * 物流联系人电话
     */
    private String logisticsContactsTel;

    /**
     * 物流联系人手机
     */
    private String logisticsContactsPhone;

    /**
     * 物流联系人qq
     */
    private String logisticsContactsQq;

    /**
     * 物流联系人邮箱
     */
    private String logisticsContactsEmail;

    /**
     * 财务联系人名字
     */
    private String financeContactsName;

    /**
     * 财务联系人电话
     */
    private String financeContactsTel;

    /**
     * 财务联系人手机
     */
    private String financeContactsPhone;

    /**
     * 财务联系人qq
     */
    private String financeContactsQq;

    /**
     * 财务联系人邮箱
     */
    private String financeContactsEmail;

    /**
     * api授权码
     */
    private String authorizationCode;

    /**
     * 账号锁定，0：正常，1：锁定
     */
    private Integer lockState;

    /**
     * 账户审核，0：未审核，1：已审核
     */
    private Integer auditState;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 账户状态(财务状态) 0：正常，1：欠费，2：禁用
     */
    private Integer accountStatus;

    /**
     * 信用额度
     */
    private BigDecimal initialCreditLimit;

    /**
     * 出库累加费用
     */
    private BigDecimal outCumulativeFee;

    /**
     * 最后收款时间
     */
    private LocalDateTime lastCollectionDate;

    /**
     * 经办人名称
     */
    private String handlerName;

    /**
     * 经办人id
     */
    private Integer handlerId;

    /**
     * 收款周期
     */
    private Integer paymentCycle;

    /**
     * 销售链接
     */
    private String salesLink;

    /**
     * 逻辑删除
     */
    private Integer deleted;
}
