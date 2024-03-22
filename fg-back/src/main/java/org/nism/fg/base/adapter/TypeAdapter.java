package org.nism.fg.base.adapter;

import org.nism.fg.domain.entity.FgTable;

import java.util.Map;

public interface TypeAdapter {

    Map<String, Object> getMap(FgTable table);

}
