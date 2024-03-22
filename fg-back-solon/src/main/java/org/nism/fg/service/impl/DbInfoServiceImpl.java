package org.nism.fg.service.impl;

import org.nism.fg.base.core.ServiceImpl;
import org.nism.fg.domain.entity.DbInfo;
import org.nism.fg.mapper.DbInfoMapper;
import org.nism.fg.service.DbInfoService;
import org.noear.solon.annotation.Component;

/**
 * 自由代码数据源业务实现层
 *
 * @author nism
 */
@Component
public class DbInfoServiceImpl extends ServiceImpl<DbInfoMapper, DbInfo> implements DbInfoService {
}
