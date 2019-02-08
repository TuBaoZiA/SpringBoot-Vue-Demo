package com.ycj.demo.exception;

import com.ycj.demo.result.CodeMsg;
import lombok.Getter;

public class SystemException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	@Getter
    private CodeMsg codeMsg;

    public SystemException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg=codeMsg;
    }

}

