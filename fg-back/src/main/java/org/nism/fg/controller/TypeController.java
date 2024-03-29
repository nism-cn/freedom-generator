package org.nism.fg.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.mvc.controller.BaseController;
import org.nism.fg.base.core.mvc.domain.BaseEntity;
import org.nism.fg.base.core.mvc.domain.R;
import org.nism.fg.domain.entity.Type;
import org.nism.fg.service.TypeService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 类型控制层
 *
 * @author nism
 * @since 1.0.1
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("type")
public class TypeController extends BaseController<TypeService, Type> {

    @GetMapping("list/{mold}")
    public R<?> listMold(@PathVariable String mold, String search, boolean dis) {
        return R.ok(baseService.lambdaQuery()
                        .eq(Type::getMold, mold)
                        .like(StrUtil.isNotBlank(search), Type::getVal, search)
                        .orderByDesc(BaseEntity::getId)
//                .eq(Type::getDis, dis)
                        .list()
        );
    }

    @GetMapping("groups")
    public R<?> groups() {
        return R.ok(baseService.query()
                .select("MOLD")
                .groupBy("MOLD")
                .orderByAsc("MAX(ID)")
                .list()
                .stream()
                .map(Type::getMold)
                .collect(Collectors.toList())
        );
    }

    @PutMapping("{id}/{dis}")
    public R<?> updateDis(@PathVariable Long id, @PathVariable Boolean dis) {
        return R.ok(baseService.lambdaUpdate().eq(BaseEntity::getId, id).set(Type::getDis, dis).update());
    }

}
