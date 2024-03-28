package org.nism.fg.service.impl;

import org.nism.fg.base.core.mvc.service.ServiceImpl;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.mapper.SetsMapper;
import org.nism.fg.service.SetsService;
import org.springframework.stereotype.Service;

/**
 * 自由代码项目设置业务实现层
 *
 * @author nism
 */
@Service
public class SetsServiceImpl extends ServiceImpl<SetsMapper, Sets> implements SetsService {
}
