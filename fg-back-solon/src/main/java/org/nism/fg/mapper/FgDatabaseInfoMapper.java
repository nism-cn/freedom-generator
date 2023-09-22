package org.nism.fg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nism.fg.domain.entity.FgDatabaseInfo;

@Mapper
public interface FgDatabaseInfoMapper extends BaseMapper<FgDatabaseInfo> {
}
