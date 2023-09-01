package com.dpdev.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.dpdev.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

}
