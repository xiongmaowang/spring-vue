package com.xmw.springvue.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Created by yogo on 2017/5/16.
 */
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
        //返回vue页面
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
        //错误类型为500，表示服务器响应错误，默认显示500.html网页
        ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
        ErrorPage throwable = new ErrorPage(Throwable.class,"/error/500");
        errorPageRegistry.addErrorPages(e404, e500, throwable);
    }
}