package org.nism.fg.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.solon.annotation.Db;
import org.nism.fg.base.core.CoreConstant;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.ServiceImpl;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.Type;
import org.nism.fg.domain.entity.TypeMap;
import org.nism.fg.mapper.TypeMapMapper;
import org.nism.fg.mapper.TypeMapper;
import org.nism.fg.service.TypeService;
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
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

    @Db
    private TypeMapMapper typeMapMapper;

    private List<String> listTypes(String mold) {
        return baseMapper.selectList(Wrappers.lambdaQuery(Type.class)
                        .eq(Type::getDis, false)
                        .eq(Type::getMold, mold)
                ).stream()
                .map(Type::getVal)
                .collect(Collectors.toList());
    }

    private Map<String, String> listMaps(String typeMold, String mapMold) {
        Map<String, String> map = new HashMap<>();
        List<Type> types = baseMapper.selectList(Wrappers.lambdaQuery(Type.class).eq(Type::getDis, false));
        Map<Long, String> typeMap = types.stream().collect(Collectors.toMap(BaseEntity::getId, Type::getVal));
        List<TypeMap> list = typeMapMapper.selectList(Wrappers.lambdaQuery(TypeMap.class).eq(TypeMap::getTypeMold, typeMold).eq(TypeMap::getMapMold, mapMold));

        for (TypeMap m : list) {
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
