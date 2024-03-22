package org.nism.fg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nism.fg.base.core.BaseMapper;
import org.nism.fg.domain.entity.FgTable;

@Mapper
public interface TableMapper extends BaseMapper<FgTable> {
}
