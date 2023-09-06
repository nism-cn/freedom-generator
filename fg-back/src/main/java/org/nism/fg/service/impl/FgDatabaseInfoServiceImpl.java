package org.nism.fg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nism.fg.domain.entity.FgDatabaseInfo;
import org.nism.fg.mapper.FgDatabaseInfoMapper;
import org.nism.fg.service.FgDatabaseInfoService;
import org.springframework.stereotype.Service;

/**
 * 自由代码数据源业务实现层
 *
 * @author nism
 */
@Service
public class FgDatabaseInfoServiceImpl extends ServiceImpl<FgDatabaseInfoMapper, FgDatabaseInfo> implements FgDatabaseInfoService {
}
