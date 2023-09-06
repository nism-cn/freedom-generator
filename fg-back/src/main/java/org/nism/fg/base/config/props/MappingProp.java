package org.nism.fg.base.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

/**
 * 字段类型映射
 *
 * @author inism
 * @since 1.0.0
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mappings")
@PropertySource("classpath:props/maps.properties")
public class MappingProp {

    private Map<String, String> columnTypeMap;
    private Map<String, String> htmlTypeMap;
    private List<String> javaType;
    private List<String> columnType;
    private List<String> selectType;
    private List<String> htmlType;

}
