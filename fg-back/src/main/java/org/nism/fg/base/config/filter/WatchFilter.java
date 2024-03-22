package org.nism.fg.base.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.CoreConstant;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        if (request.getRequestURI().contains("/io/log/")) {
            filterChain.doFilter(request, response);
            return;
        }

        StopWatch watch = new StopWatch();
        String taskName = String.format("[%s]%s", request.getMethod(), request.getRequestURI());

        watch.start(taskName);
        filterChain.doFilter(request, response);
        watch.stop();

        StopWatch.TaskInfo task = watch.getLastTaskInfo();
        log.debug("###### 请求耗时\t: {}s,\t {}ms \t - 请求: {}",
                String.format("%.2f", task.getTimeSeconds()),
                task.getTimeMillis(),
                task.getTaskName()
        );
        response.addHeader("watch-time", task.getTimeSeconds() + "s");
    }

}