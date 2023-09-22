package org.nism.fg.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件数据传输对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
public class DictDTO implements Serializable {

    private String name;
    private String val;

}
