package org.nism.fg.service.impl;

import org.nism.fg.base.core.ServiceImpl;
import org.nism.fg.domain.entity.FgProjectSetting;
import org.nism.fg.mapper.FgProjectSettingMapper;
import org.nism.fg.service.FgProjectSettingService;
import org.springframework.stereotype.Service;

/**
 * 自由代码项目设置业务实现层
 *
 * @author nism
 */
@Service
public class FgProjectSettingServiceImpl extends ServiceImpl<FgProjectSettingMapper, FgProjectSetting> implements FgProjectSettingService {
}
