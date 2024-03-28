package org.nism.fg.service.impl;

import org.nism.fg.base.core.mvc.service.ServiceImpl;
import lombok.AllArgsConstructor;
import org.nism.fg.domain.entity.TypeMap;
import org.nism.fg.mapper.TypeMapMapper;
import org.nism.fg.service.TypeMapService;
import org.springframework.stereotype.Service;

/**
 * 类型映射表业务实现层
 *
 * @author nism
 * @since 1.0.1
 */
@AllArgsConstructor
@Service
public class TypeMapServiceImpl extends ServiceImpl<TypeMapMapper, TypeMap> implements TypeMapService {

}
