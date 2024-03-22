package org.nism.fg.service;

import org.nism.fg.base.core.IService;
import org.nism.fg.domain.entity.Proj;

import java.util.List;

public interface ProjService extends IService<Proj> {

    void importTables(Long dbId, List<String> tableNames);

}
