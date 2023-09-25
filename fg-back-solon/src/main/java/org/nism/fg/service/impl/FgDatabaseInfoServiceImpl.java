package org.nism.fg.service.impl;

import org.nism.fg.base.core.ServiceImpl;
import org.nism.fg.domain.entity.FgDatabaseInfo;
import org.nism.fg.mapper.FgDatabaseInfoMapper;
import org.nism.fg.service.FgDatabaseInfoService;
import org.noear.solon.annotation.Component;

/**
 * 自由代码数据源业务实现层
 *
 * @author nism
 */
@Component
public class FgDatabaseInfoServiceImpl extends ServiceImpl<FgDatabaseInfoMapper, FgDatabaseInfo> implements FgDatabaseInfoService {
}
