package org.nism.fg.service.impl;

import org.nism.fg.base.core.mvc.service.ServiceImpl;
import org.nism.fg.domain.entity.Config;
import org.nism.fg.mapper.ConfigMapper;
import org.nism.fg.service.ConfigService;
import org.springframework.stereotype.Service;

/**
 * 自由代码数据源业务实现层
 *
 * @author nism
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
}
