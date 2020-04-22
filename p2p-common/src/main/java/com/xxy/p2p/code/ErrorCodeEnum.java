package com.xxy.p2p.code;

import com.xxy.p2p.base.ErrorCodeInter;

public enum ErrorCodeEnum implements ErrorCodeInter {
    X01("X01", "用户不存在"),
    X02("X02", "密码错误"),
    X03("X03", "用户已存在"),
    X04("X04", "请重新登录")
    ;

    private String code;

    private String desc;

    ErrorCodeEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String var1) {
        this.desc = var1;
    }

    @Override
    public String getCode() {
       return code;
    }

    @Override
    public void setCode(String var1) {
        this.code = var1;
    }
}
