package org.nism.fg.base.core.adapter;


import org.nism.fg.domain.entity.Table;

import java.util.Map;

public interface TypeAdapter {

    Map<String, Object> getMap(Table table);

}
