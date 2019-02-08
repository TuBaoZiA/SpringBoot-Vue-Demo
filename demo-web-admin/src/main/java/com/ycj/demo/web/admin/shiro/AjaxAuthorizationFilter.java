package com.ycj.demo.web.admin.shiro;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 对没有登录的请求进行拦截, 全部返回json信息. 覆盖掉shiro原本的跳转login.jsp的拦截方式
 */
public class AjaxAuthorizationFilter extends FormAuthenticationFilter {

    /*private void render(HttpServletResponse response,Result sessionError) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream=response.getOutputStream();
        String str= new ObjectMapper().writeValueAsString(sessionError);
        outputStream.write(str.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        render((HttpServletResponse) response, Result.error(CodeMsg.UNAUTHENTICATED));
        return false;
    }

    @Bean
    public FilterRegistrationBean registration(AjaxAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }*/

}
