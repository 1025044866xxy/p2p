package com.xxy.p2p.code;

import com.xxy.p2p.base.ErrorCodeInter;

public enum ErrorCodeEnum implements ErrorCodeInter {
    X01("X01", "用户不存在"),
    X02("X02", "用户名与密码不匹配"),
    X03("X03", "用户已存在"),
    X04("X04", "请重新登录"),





    P01("P01","参数缺失"),

    E01("E01", "发生错误"),

    Q01("Q01", "您目前的资质审核不通关,暂时无法进行放贷,请选择其他项目类型或调整金额再次尝试!"),
    Q02("Q02","该笔贷款已经还清!"),
    Q03("Q03","偿还金额超出剩余待还金额"),
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
