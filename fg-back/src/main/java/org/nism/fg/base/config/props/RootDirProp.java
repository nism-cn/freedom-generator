package org.nism.fg.base.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 字段类型映射
 *
 * @author inism
 * @since 1.0.0
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "root-dir")
@PropertySource("classpath:props/settings.properties")
public class RootDirProp {

    private String sql;
    private String java;
    private String mapper;
    private String js;
    private String vue;
    private String html;

}
