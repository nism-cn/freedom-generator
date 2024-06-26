package org.nism.fg.base.core.adapter.impl;

import org.nism.fg.base.core.adapter.TypeAdapter;
import org.nism.fg.base.core.BaseConstant;
import org.nism.fg.domain.entity.Table;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component(BaseConstant.TYPE_ADAPTER_PREFIX + PythonAdapter.TYPE)
public class PythonAdapter implements TypeAdapter {

    protected static final String TYPE = "python";

    public Map<String, Object> getMap(Table table) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", TYPE);
        return map;
    }


}
