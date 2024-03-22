package org.nism.fg.base.core.adapter.impl;

import org.nism.fg.base.core.adapter.TypeAdapter;
import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.domain.entity.FgTable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component(CoreConstant.TYPE_ADAPTER_PREFIX + JsAdapter.TYPE)
public class JsAdapter implements TypeAdapter {

    protected static final String TYPE = "js";

    public Map<String, Object> getMap(FgTable table) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", TYPE);
        return map;
    }


}
