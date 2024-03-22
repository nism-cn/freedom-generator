package org.nism.fg.service.impl;

import org.nism.fg.base.core.ServiceImpl;
import org.nism.fg.domain.entity.TypeMap;
import org.nism.fg.mapper.TypeMapMapper;
import org.nism.fg.service.TypeMapService;
import org.noear.solon.annotation.Component;

/**
 * 类型映射表业务实现层
 *
 * @author nism
 * @since 1.0.1
 */
@Component
public class TypeMapServiceImpl extends ServiceImpl<TypeMapMapper, TypeMap> implements TypeMapService {

}
