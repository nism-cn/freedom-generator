package org.nism.fg.base.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.util.Utils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * Druid配置
 *
 * @author inism
 * @since 1.0.0
 */
@Configuration
public class DruidConfig {

    private static final String PATTERN = "/druid/*";

    /**
     * 配置 Druid 监控管理后台的Servlet；
     * 内置 Servlet 容器时没有web.xml文件，所以使用 Spring Boot 的注册 Servlet 方式
     * 后台监控功能
     */
    @Bean
    public ServletRegistrationBean<?> statViewServlet() {
        return new ServletRegistrationBean<>(new StatViewServlet(), PATTERN);
    }

    @Bean
    public FilterRegistrationBean<?> removeDruidAdFilterRegistrationBean() {
        // 提取common.js的配置路径
        String commonJsPattern = PATTERN.replaceAll("\\*", "js/common.js");
        final String filePath = "support/http/resources/js/common.js";

        //创建filter进行过滤
        Filter filter = (request, response, chain) -> {
            chain.doFilter(request, response);
            // 重置缓冲区，响应头不会被重置
            response.resetBuffer();
            // 获取common.js
            String text = Utils.readFromResource(filePath);
            // 正则替换banner, 除去底部的广告信息
            text = text.replaceAll("<a.*?banner\"></a><br/>", "");
            text = text.replaceAll("powered.*?shrek.wang</a>", "");
            response.getWriter().write(text);
        };
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }

}
