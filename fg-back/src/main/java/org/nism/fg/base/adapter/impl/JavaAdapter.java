package org.nism.fg.base.adapter.impl;

import org.nism.fg.base.adapter.TypeAdapter;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.domain.entity.FgTable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component(CoreConstant.TYPE_ADAPTER_PREFIX + JavaAdapter.TYPE)
public class JavaAdapter implements TypeAdapter {

    protected static final String TYPE = "java";

    public Map<String, Object> getMap(FgTable table) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", TYPE);
        return map;
    }


}