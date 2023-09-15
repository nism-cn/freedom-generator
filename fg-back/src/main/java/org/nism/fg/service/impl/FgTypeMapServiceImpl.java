package org.nism.fg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.nism.fg.domain.entity.FgTypeMap;
import org.nism.fg.mapper.FgTypeMapMapper;
import org.nism.fg.service.FgTypeMapService;
import org.springframework.stereotype.Service;

/**
 * 类型映射表业务实现层
 *
 * @author nism
 * @since 1.0.1
 */
@AllArgsConstructor
@Service
public class FgTypeMapServiceImpl extends ServiceImpl<FgTypeMapMapper, FgTypeMap> implements FgTypeMapService {

}
