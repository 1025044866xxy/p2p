package com.xxy.p2p.base;

public class ErrorResponse<T> {
    private static final long serialVersionUID = 5706330384328182339L;
    private String errorCode;
    private String errorDesc;
    private T result;

    private boolean success = true;

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return this.errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setErrorCode(ErrorCodeInter errorCodeEnum) {
        this.setSuccess(false);
        this.setErrorCode(errorCodeEnum.getCode());
        this.setErrorDesc(errorCodeEnum.getDesc());
    }

    public ErrorResponse() {
        this.setSuccess(false);
    }

    public ErrorResponse(ErrorCodeInter errorCodeEnum) {
        this.setSuccess(false);
        this.setErrorCode(errorCodeEnum.getCode());
        this.setErrorDesc(errorCodeEnum.getDesc());
    }

    public ErrorResponse(String errorDesc) {
        this.setSuccess(false);
        this.errorDesc = errorDesc;
    }

}
