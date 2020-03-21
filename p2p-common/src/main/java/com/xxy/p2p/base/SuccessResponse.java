package com.xxy.p2p.base;

import lombok.Data;

@Data
public class SuccessResponse<T> {

    private static final long serialVersionUID = 2333333333L;
    private T result;
    private Boolean success = true;

    public SuccessResponse() {
    }

    public SuccessResponse(T result) {
        this.result = result;
    }

}
