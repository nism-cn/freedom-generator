package org.nism.fg.controller;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.Type;
import org.nism.fg.service.TypeService;
import org.noear.solon.annotation.*;

/**
 * 类型控制层
 *
 * @author nism
 * @since 1.0.1
 */
@Slf4j
@Controller
@Mapping("type")
public class TypeController {

    @Inject
    private TypeService baseService;

    @Get
    @Mapping("list/{mold}")
    public R<?> listMold(String mold, String search, boolean dis) {
        return R.ok(baseService.lambdaQuery()
                .eq(Type::getMold, mold)
                .like(StrUtil.isNotBlank(search), Type::getVal, search)
                .orderByDesc(BaseEntity::getId)
                .eq(Type::getDis, dis)
                .list()
        );
    }

    @Get
    @Mapping
    public R<?> find() {
        return R.ok(baseService.lambdaQuery().eq(Type::getDis, false).list());
    }

    @Get
    @Mapping("{id}")
    public R<?> findOne(Long id) {
        return R.ok(baseService.lambdaQuery().eq(BaseEntity::getId, id).eq(Type::getDis, false).one());
    }

    @Post
    @Mapping
    public R<?> create(Type e) {
        return R.ok(baseService.save(e));
    }

    @Put
    @Mapping("{id}")
    public R<?> update(Type e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Put
    @Mapping("{id}/{dis}")
    public R<?> updateDis(Long id, Boolean dis) {
        return R.ok(baseService.lambdaUpdate().eq(BaseEntity::getId, id).set(Type::getDis, dis).update());
    }

    @Post
    @Mapping("saveOrUpdate")
    public R<?> saveOrUpdate(Type e) {
        return R.ok(baseService.saveOrUpdate(e));
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(Long id) {
        return R.ok(baseService.removeById(id));
    }

}
