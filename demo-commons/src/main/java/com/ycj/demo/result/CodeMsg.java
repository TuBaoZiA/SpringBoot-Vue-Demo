package com.ycj.demo.result;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CodeMsg {

    @Getter private int code;

    @Getter private String msg;

    public static final CodeMsg SUCCESS = new CodeMsg(20000,"success！");

    public static final CodeMsg BIND_ERROR = new CodeMsg(101, "参数校验失败：%s");

	public static final CodeMsg SERVER_ERROR = new CodeMsg(103, "服务器异常！");

    public static final CodeMsg ERROR_PATH = new CodeMsg(104, "请求路径不存在");

    public static final CodeMsg HTTP_METHOD_ERROR = new CodeMsg(105, "HTTP method error");

    public static final CodeMsg UNAUTHENTICATED = new CodeMsg(106, "你尚未登录或登陆已过期,请登陆后再执行操作！");

    public static final CodeMsg LOGIN_ERROR = new CodeMsg(107,"认证失败，用户名或密码错误");

    public static final CodeMsg UNAUTHORIZED = new CodeMsg(108, "权限不足");


    public static final CodeMsg PASSWORD_ERROR = new CodeMsg(109, "源密码错误");

    public static final CodeMsg USERNAME_EXIST = new CodeMsg(110, "用户名已存在");

    public static final CodeMsg ROLENAME_EXIST = new CodeMsg(111, "角色名称已存在");

    public static final CodeMsg MENU_ERROR = new CodeMsg(112, "");


    public static final CodeMsg JOB_EXIST = new CodeMsg(301, "Job已经存在, jobName:%s,jobGroup:%s");

    public static final CodeMsg JOB_NOTEXIST = new CodeMsg(302, "Job不存在, jobName:%s,jobGroup:%s");

    public static final CodeMsg JOB_ERROR = new CodeMsg(303, "执行失败：%s");

    public static final CodeMsg JOBCLASS_NOTEXIST = new CodeMsg(304, "Job未找到！");

    public static final CodeMsg SCHEDULER_EXCEPTION = new CodeMsg(305, "表达式执行错误!");


    public static final CodeMsg MAIL_SEND_ERROR = new CodeMsg(401, "邮件发送失败：%s");

    public CodeMsg fillArgs(Object... args) {
		return new CodeMsg(code, String.format(this.msg, args));
	}

	public CodeMsg setMessage(String message){
        this.msg = message;
        return this;
    }
}
