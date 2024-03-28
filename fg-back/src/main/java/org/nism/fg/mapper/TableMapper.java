package org.nism.fg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nism.fg.base.core.mvc.mapper.BaseMapper;
import org.nism.fg.domain.entity.Table;

@Mapper
public interface TableMapper extends BaseMapper<Table> {
}
