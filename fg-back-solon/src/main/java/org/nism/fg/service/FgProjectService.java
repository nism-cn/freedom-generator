package org.nism.fg.service;

import com.baomidou.mybatisplus.solon.service.IService;
import org.nism.fg.domain.entity.FgProject;

import java.util.List;

public interface FgProjectService extends IService<FgProject> {

    void importTables(Long dbId, List<String> tableNames);

}
