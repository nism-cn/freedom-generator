package org.nism.fg.base.filter;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.constant.CoreConstant;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 请求计时器
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
@Profile(CoreConstant.ENV_DEV)
@Component
public class WatchFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        StopWatch watch = new StopWatch();
        String taskName = String.format("[%s]%s", request.getMethod(), request.getRequestURI());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.debug("--- 请求开始计时\t 时间: {} \t - 请求: {}", dtf.format(LocalDateTime.now()), taskName);
        watch.start(taskName);
        filterChain.doFilter(request, response);
        watch.stop();

        StopWatch.TaskInfo task = watch.getLastTaskInfo();
        log.debug("--- 请求计时结束\t 耗时: {}s,\t {}ms \t - 请求: {}", task.getTimeSeconds(), task.getTimeMillis(), task.getTaskName());
        response.addHeader("watch-time", task.getTimeSeconds() + "s");
    }

}