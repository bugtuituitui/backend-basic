package com.example.common.config;

import com.example.common.exception.BusinessException;
import com.example.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    private static final String SQL_EXCEPTION = "数据库异常";
    private static final String PARAMETER_EXCEPTION = "参数异常";
    private static final String UNKNOWN_EXCEPTION = "系统异常";
    private static final String BIND_EXCEPTION = "自定义参数验证异常";
    private static final String RUNTIME_EXCEPTION = "未知运行时异常";
    private static final String REQUEST_METHOD_EXCEPTION = "请求方式不支持";
    private static final String AUTH_EXCEPTION = "权限校验失败";

    // 权限校验异常
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return Result.fail(AUTH_EXCEPTION);
    }

    // 请求方式不支持异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                      HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return Result.fail(REQUEST_METHOD_EXCEPTION);
    }

    // 自定义验证异常
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.fail(BIND_EXCEPTION);
    }

    // 自定义验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.fail(BIND_EXCEPTION);
    }


    // 数据库处理异常
    @ExceptionHandler(SQLException.class)
    public Result handleSQLException(SQLException e) {
        log.error(e.getMessage() + e.getCause());
        return Result.fail(SQL_EXCEPTION);
    }

    // 自定义异常
    @ExceptionHandler(BusinessException.class)
    public Result handleBaseException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生自定义异常.", requestURI, e);
        return Result.fail(e.getMessage());
    }

    // 运行时异常
    @ExceptionHandler(value = RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Result.fail(RUNTIME_EXCEPTION);
    }

    // 系统异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Result.fail(UNKNOWN_EXCEPTION);
    }


}