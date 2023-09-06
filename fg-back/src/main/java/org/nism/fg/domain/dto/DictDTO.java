package org.nism.fg.domain.dto;

import lombok.Data;
import org.nism.fg.base.core.Tabs;
import org.nism.fg.base.core.Tree;
import org.nism.fg.domain.enums.TabsType;

import java.io.Serializable;
import java.util.List;

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
