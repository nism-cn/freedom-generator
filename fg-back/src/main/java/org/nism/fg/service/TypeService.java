package org.nism.fg.service;

import org.nism.fg.base.core.IService;
import org.nism.fg.domain.entity.Type;

import java.util.Map;

public interface TypeService extends IService<Type> {

    Map<String, Map<String, String>> loadMaps();

    Map<String, String> loadMaps(String type);

}
