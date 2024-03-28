package org.nism.fg.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.nism.fg.base.core.mvc.service.ServiceImpl;
import org.nism.fg.domain.entity.Type;
import org.nism.fg.domain.entity.TypeMap;
import org.nism.fg.mapper.TypeMapMapper;
import org.nism.fg.mapper.TypeMapper;
import org.nism.fg.service.TypeService;
import org.springframework.stereotype.Service;

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
@AllArgsConstructor
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

    private final TypeMapMapper typeMapMapper;

    /**
     *
     * @return map.get("java").get("varchar") ==> String
     */
    @Override
    public Map<String, Map<String, String>> loadMaps() {
        Map<String, Map<String, String>> maps = new HashMap<>();
        for (TypeMap m : getJoin()) {
            String key = m.getMapMold();
            Map<String, String> map = maps.computeIfAbsent(key, k -> new HashMap<>());
            map.put(m.getTypeVal(), m.getMapVal());
        }
        return maps;
    }

    /**
     *
     * @return maps.get("java") ==> String
     */
    @Override
    public Map<String, String> loadMaps(String type) {
        type = type.toLowerCase();
        Map<String, Map<String, String>> maps = new HashMap<>();
        for (TypeMap m : getJoin()) {
            String key = m.getTypeVal().toLowerCase();
            Map<String, String> map = maps.computeIfAbsent(key, k -> new HashMap<>());
            map.put(m.getMapMold(), m.getMapVal());
        }
        return maps.computeIfAbsent(type, k -> new HashMap<>());
    }

    private List<TypeMap> getJoin() {
        List<Type> types = baseMapper.selectList(Wrappers.emptyWrapper());
        Map<Long, String> typeMap = types.stream().collect(Collectors.toMap(Type::getId, Type::getVal));
        List<TypeMap> list = typeMapMapper.selectList(Wrappers.emptyWrapper());
        for (TypeMap m : list) {
            m.setTypeVal(typeMap.get(m.getTypeId()));
            m.setMapVal(typeMap.get(m.getMapId()));
        }
        return list;
    }

}
