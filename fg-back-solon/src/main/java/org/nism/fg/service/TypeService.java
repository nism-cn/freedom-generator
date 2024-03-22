package org.nism.fg.service;

import org.nism.fg.base.core.IService;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.Type;

public interface TypeService extends IService<Type> {

    MapDTO loadMap();

}
