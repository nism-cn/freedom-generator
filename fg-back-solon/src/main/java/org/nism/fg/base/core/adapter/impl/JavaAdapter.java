package org.nism.fg.base.core.adapter.impl;

import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.base.core.adapter.TypeAdapter;
import org.nism.fg.domain.entity.Table;
import org.noear.solon.annotation.Component;

import java.util.HashMap;
import java.util.Map;

@Component(CoreConstant.TYPE_ADAPTER_PREFIX + JavaAdapter.TYPE)
public class JavaAdapter implements TypeAdapter {

    protected static final String TYPE = "java";

    public Map<String, Object> getMap(Table table) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", TYPE);
        return map;
    }


}
