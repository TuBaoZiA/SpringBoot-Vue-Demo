package com.ycj.demo.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /*错误码*/
    //@JsonProperty(value = "status") 指定转换json后属性的名称
    @Getter private Integer code;
    /*提示信息*/
    @Getter private String msg;

    @Getter private Integer count;
    /*具体的内容*/
    @Getter private T data;

    private Result(T data){
        this(data, 0);
    }

    private Result(T data,Integer count){
       this(CodeMsg.SUCCESS.getCode(), CodeMsg.SUCCESS.getMsg(), count, data);
    }

    private Result(Integer code,String msg){
        this(code,msg,0,null);
    }

    public static <T> Result success(T data){
        return new Result(data);
    }

    public static <T> Result success(){
        return new Result(null);
    }

    public static <T> Result success(T data,Integer count){
        return new Result(data,count);
    }

    public static Result error(CodeMsg msg){
        return new Result(msg.getCode(),msg.getMsg());
    }

    public static Result error(String msg){
        return new Result(500 ,msg);
    }
    	
}
