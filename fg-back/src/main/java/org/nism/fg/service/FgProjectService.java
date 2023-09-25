package org.nism.fg.service;

import org.nism.fg.base.core.IService;
import org.nism.fg.domain.entity.FgProject;

import java.io.Serializable;
import java.util.List;

public interface FgProjectService extends IService<FgProject> {

    void importTables(Serializable dbId, List<String> tableNames);

}
