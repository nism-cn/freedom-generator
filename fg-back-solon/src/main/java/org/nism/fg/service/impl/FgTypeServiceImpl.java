package org.nism.fg.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.solon.service.impl.ServiceImpl;
import org.apache.ibatis.solon.annotation.Db;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.FgType;
import org.nism.fg.domain.entity.FgTypeMap;
import org.nism.fg.mapper.FgTypeMapMapper;
import org.nism.fg.mapper.FgTypeMapper;
import org.nism.fg.service.FgTypeService;
import org.noear.solon.annotation.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类型表业务实现层
 *
 * @author nism
 * @since 1.0.1
 */
@Component
public class FgTypeServiceImpl extends ServiceImpl<FgTypeMapper, FgType> implements FgTypeService {

    @Db
    private FgTypeMapMapper typeMapMapper;

    private List<String> listTypes(String mold) {
        return baseMapper.selectList(Wrappers.lambdaQuery(FgType.class)
                        .eq(FgType::getDis, false)
                        .eq(FgType::getMold, mold)
                ).stream()
                .map(FgType::getVal)
                .collect(Collectors.toList());
    }

    private Map<String, String> listMaps(String typeMold, String mapMold) {
        Map<String, String> map = new HashMap<>();
        List<FgType> fgTypes = baseMapper.selectList(Wrappers.lambdaQuery(FgType.class).eq(FgType::getDis, false));
        Map<Long, String> typeMap = fgTypes.stream().collect(Collectors.toMap(BaseEntity::getId, FgType::getVal));
        List<FgTypeMap> list = typeMapMapper.selectList(Wrappers.lambdaQuery(FgTypeMap.class).eq(FgTypeMap::getTypeMold, typeMold).eq(FgTypeMap::getMapMold, mapMold));

        for (FgTypeMap m : list) {
            map.put(typeMap.get(m.getTypeId()), typeMap.get(m.getMapId()));
        }
        return map;
    }

    @Override
    public MapDTO loadMap() {
        MapDTO prop = new MapDTO();
        prop.setColumnType(this.listTypes(CoreConstant.TYPE_DB));
        prop.setJavaType(this.listTypes(CoreConstant.TYPE_JAVA));
        prop.setHtmlType(this.listTypes(CoreConstant.TYPE_HTML));
        prop.setSelectType(this.listTypes(CoreConstant.TYPE_SQL));
        prop.setColumnTypeMap(this.listMaps(CoreConstant.TYPE_DB, CoreConstant.TYPE_JAVA));
        prop.setHtmlTypeMap(this.listMaps(CoreConstant.TYPE_DB, CoreConstant.TYPE_HTML));
        return prop;
    }


}
