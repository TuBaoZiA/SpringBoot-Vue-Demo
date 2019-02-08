package com.ycj.demo.exception;

import com.ycj.demo.result.CodeMsg;
import lombok.Getter;

public class BusinessException extends RuntimeException{
    @Getter
    private CodeMsg codeMsg;

    public BusinessException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg=codeMsg;
    }
}
