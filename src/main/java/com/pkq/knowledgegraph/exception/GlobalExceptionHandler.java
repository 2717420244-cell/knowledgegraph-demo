package com.pkq.knowledgegraph.exception;

import com.pkq.knowledgegraph.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice    // 拦截所有 Controller 抛出的异常
public class GlobalExceptionHandler {

    // 处理自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    // 处理参数校验失败（@Valid 不通过时抛的）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)   // HTTP 状态码 400
    public Result<Void> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)   // 取出 @NotBlank(message="...") 里的消息
                .collect(Collectors.joining(", "));
        return Result.error(400, msg);
    }

    // 处理登录失败
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)  // HTTP 状态码 401
    public Result<Void> handleBadCredentials(BadCredentialsException e) {
        return Result.error(401, e.getMessage());
    }

    // 处理用户不存在
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleUserNotFound(UsernameNotFoundException e) {
        return Result.error(401, e.getMessage());
    }

    // 兜底：处理所有未预料的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);    // 日志记录完整堆栈
        return Result.error("服务器内部错误，请稍后重试");  // 但只给前端返回模糊信息
    }
}