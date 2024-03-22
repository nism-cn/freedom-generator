package org.nism.fg.service.impl;

import org.nism.fg.base.core.ServiceImpl;
import org.nism.fg.domain.entity.DbInfo;
import org.nism.fg.mapper.DbInfoMapper;
import org.nism.fg.service.DbInfoService;
import org.springframework.stereotype.Service;

/**
 * 自由代码数据源业务实现层
 *
 * @author nism
 */
@Service
public class DbInfoServiceImpl extends ServiceImpl<DbInfoMapper, DbInfo> implements DbInfoService {
}
