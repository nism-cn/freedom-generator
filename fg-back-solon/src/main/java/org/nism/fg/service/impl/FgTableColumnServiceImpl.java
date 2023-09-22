package org.nism.fg.service.impl;

import com.baomidou.mybatisplus.solon.service.impl.ServiceImpl;
import org.nism.fg.domain.entity.FgTableColumn;
import org.nism.fg.mapper.FgTableColumnMapper;
import org.nism.fg.service.FgTableColumnService;
import org.noear.solon.annotation.Component;

/**
 * 自由代码表字段业务实现层
 *
 * @author nism
 */
@Component
public class FgTableColumnServiceImpl extends ServiceImpl<FgTableColumnMapper, FgTableColumn> implements FgTableColumnService {
}
