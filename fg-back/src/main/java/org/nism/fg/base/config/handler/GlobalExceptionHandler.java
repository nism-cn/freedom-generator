package org.nism.fg.base.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.nism.fg.base.core.mvc.domain.R;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> exception(HttpRequestMethodNotSupportedException e) {
        log.error("不支持'{}'请求", e.getMethod());
        return R.fail(e.getMessage());
    }

    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R<Void> exception(DuplicateKeyException e) {
        log.error("数据库中已存在记录'{}'", e.getMessage());
        return R.fail("数据库中已存在该记录，请联系管理员确认");
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public R<Void> exception(MyBatisSystemException e) {
        log.error("Mybatis系统异常", e);
        return R.fail(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> exception(RuntimeException e) {
        log.error("发生异常", e);
        return R.fail(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> exception(Exception e) {
        log.error("发生系统异常.", e);
        return R.fail(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> exception(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return R.fail(message);
    }
}
