package com.ycj.demo.web.admin.component.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycj.demo.result.CodeMsg;
import com.ycj.demo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.ycj.demo.constant.SystemConstant.TOKEN;


@Slf4j
public class JWTFilter extends AuthenticatingFilter {

    private void render(HttpServletResponse response, Result sessionError) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream=response.getOutputStream();
        String str= new ObjectMapper().writeValueAsString(sessionError);
        outputStream.write(str.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(httpServletRequest.getHeader(TOKEN) == null){
            render((HttpServletResponse) response, Result.error(CodeMsg.UNAUTHENTICATED));
            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        try {
            render((HttpServletResponse) response, Result.error(CodeMsg.UNAUTHENTICATED));
        } catch (IOException e1) {
            log.error(e1.getMessage());
        }
        return super.onLoginFailure(token, e, request, response);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response){
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(TOKEN);
        return new JWTToken(token);
    }

}
