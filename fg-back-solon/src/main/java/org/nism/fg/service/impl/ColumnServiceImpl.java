package org.nism.fg.service.impl;

import org.nism.fg.base.core.ServiceImpl;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.mapper.ColumnMapper;
import org.nism.fg.service.ColumnService;
import org.noear.solon.annotation.Component;

/**
 * 自由代码表字段业务实现层
 *
 * @author nism
 */
@Component
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column> implements ColumnService {
}
