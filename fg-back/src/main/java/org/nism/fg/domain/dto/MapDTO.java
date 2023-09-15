package org.nism.fg.domain.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 类型映射
 *
 * @author inism
 * @since 1.0.0 -> 1.0.1
 */
@Data
public class MapDTO {

    private Map<String, String> columnTypeMap;
    private Map<String, String> htmlTypeMap;
    private List<String> javaType;
    private List<String> columnType;
    private List<String> selectType;
    private List<String> htmlType;

}
