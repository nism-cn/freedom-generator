package org.nism.fg.domain.convert;

import cn.hutool.db.Entity;
import org.nism.fg.base.utils.StringUtils;
import org.nism.fg.domain.dto.DictDTO;

public class DictConvert {

    public static DictDTO to(Entity e) {
        String name = e.getStr("name");
        String val = e.getStr("val");
        if (StringUtils.isBlank(name) || StringUtils.isBlank(val)) {
            return null;
        }
        DictDTO dto = new DictDTO();
        dto.setName(name);
        dto.setVal(val);
        return dto;
    }

}
