package org.nism.fg.service;

import org.nism.fg.base.core.IService;
import org.nism.fg.domain.entity.Proj;

import java.io.Serializable;
import java.util.List;

public interface ProjService extends IService<Proj> {

    void importTables(Serializable dbId, List<String> tableNames);

}
