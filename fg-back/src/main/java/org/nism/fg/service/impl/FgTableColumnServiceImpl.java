package org.nism.fg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nism.fg.domain.entity.FgTableColumn;
import org.nism.fg.mapper.FgTableColumnMapper;
import org.nism.fg.service.FgTableColumnService;
import org.springframework.stereotype.Service;

/**
 * 自由代码表字段业务实现层
 *
 * @author nism
 */
@Service
public class FgTableColumnServiceImpl extends ServiceImpl<FgTableColumnMapper, FgTableColumn> implements FgTableColumnService {
}
