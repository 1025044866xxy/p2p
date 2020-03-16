package com.xxy.p2p.base;

public class SuccessResponse<T> {

    private static final long serialVersionUID = 2333333333L;
    private T result;

    public SuccessResponse() {
    }

    public SuccessResponse(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
