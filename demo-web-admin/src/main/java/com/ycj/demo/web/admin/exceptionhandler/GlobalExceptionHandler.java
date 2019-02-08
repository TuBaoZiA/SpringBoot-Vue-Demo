package com.ycj.demo.web.admin.exceptionhandler;

import com.ycj.demo.exception.BusinessException;
import com.ycj.demo.exception.SystemException;
import com.ycj.demo.result.CodeMsg;
import com.ycj.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.mail.MessagingException;

/**
  常见异常处理并返回相应错误码
  SpringMVC自定义异常对应的status code
  Exception                               HTTP Status Code
  ConversionNotSupportedException         500 (Internal Server Error)
  HttpMessageNotWritableException         500 (Internal Server Error)
  HttpMediaTypeNotSupportedException      415 (Unsupported Media Type)
  HttpMediaTypeNotAcceptableException     406 (Not Acceptable)
  HttpRequestMethodNotSupportedException  405 (Method Not Allowed)
  NoSuchRequestHandlingMethodException    404 (Not Found)
  TypeMismatchException                   400 (Bad Request)
  HttpMessageNotReadableException         400 (Bad Request)
  MissingServletRequestParameterException 400 (Bad Request)
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public Result exceptionHandler(Exception exception)  {
		log.error("异常信息："+exception.getMessage());
		return Result.error(CodeMsg.SERVER_ERROR);
	}

	@ExceptionHandler(value= SystemException.class)
	public Result systemExceptionHandler(SystemException exception)  {
		return Result.error(exception.getCodeMsg());
	}

	@ExceptionHandler(value= BusinessException.class)
	public Result businessExceptionHandler(BusinessException exception)  {
		return Result.error(exception.getCodeMsg());
	}

	@ExceptionHandler(value= MethodArgumentNotValidException.class)
	public Result bindExceptionHandler(MethodArgumentNotValidException exception)  {
		String msg=exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
	}

	/**
	 * 邮件发送失败
	 * @return
	 */
	@ExceptionHandler(value= MessagingException.class)
	public Result messagingExceptionHandler(MessagingException exception)  {
		return Result.error(CodeMsg.MAIL_SEND_ERROR.fillArgs(exception.getMessage()));
	}

	/**
	 * HTTP请求方法错误的拦截器
	 * 因为开发时可能比较常见,而且发生在进入controller之前,上面的拦截器拦截不到这个错误
	 * 所以定义了这个拦截器
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result httpRequestMethodHandler() {
		return Result.error(CodeMsg.HTTP_METHOD_ERROR);
	}

	/**
	 * 权限不足报错拦截
     */
	@ExceptionHandler(UnauthorizedException.class)
	public Result unauthorizedExceptionHandler(){
		return Result.error(CodeMsg.UNAUTHORIZED);
	}

	/**
	 * 认证异常
	 */
	@ExceptionHandler(UnauthenticatedException.class)
	public Result unauthenticatedExceptionHandler(){
		return Result.error(CodeMsg.UNAUTHORIZED);
	}
}
