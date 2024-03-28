package org.nism.fg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nism.fg.base.core.mvc.mapper.BaseMapper;
import org.nism.fg.domain.entity.Column;

@Mapper
public interface ColumnMapper extends BaseMapper<Column> {
}
