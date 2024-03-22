package org.nism.fg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nism.fg.base.core.BaseMapper;
import org.nism.fg.domain.entity.DbInfo;

@Mapper
public interface DbInfoMapper extends BaseMapper<DbInfo> {
}
