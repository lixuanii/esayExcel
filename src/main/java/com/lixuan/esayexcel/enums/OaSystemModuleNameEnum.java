package com.lixuan.esayexcel.enums;

/**
 * 模块名称枚举类
 */
public enum OaSystemModuleNameEnum {

    CUSTOMER_LIST("customerInfo","客户信息"),
    USER_INFO("userModule", "用户信息"),
    MESSAGE_RECORD_TEMPLATE("messageRecordTemplate","消息记录模板");

    private final String module;

    private final String desc;

    OaSystemModuleNameEnum(String module,String desc) {
        this.module = module;
        this.desc = desc;
    }

    public String getModule() {
        return module;
    }

    public String getDesc() {
        return desc;
    }
}
