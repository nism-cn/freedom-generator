package org.nism.fg.controller;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.BaseEntity;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgType;
import org.nism.fg.service.FgTypeService;
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
public class FgTypeController {

    @Inject
    private FgTypeService baseService;

    @Mapping("list/{mold}")
    public R<?> listMold(String mold, String search, boolean dis) {
        return R.ok(baseService.lambdaQuery()
                .eq(FgType::getMold, mold)
                .like(StrUtil.isNotBlank(search), FgType::getVal, search)
                .orderByDesc(BaseEntity::getId)
                .eq(FgType::getDis, dis)
                .list()
        );
    }

    @Mapping
    public R<?> find() {
        return R.ok(baseService.lambdaQuery().eq(FgType::getDis, false).list());
    }

    @Mapping("{id}")
    public R<?> findOne(Long id) {
        return R.ok(baseService.lambdaQuery().eq(BaseEntity::getId, id).eq(FgType::getDis, false).one());
    }

    @Post
    @Mapping
    public R<?> create(FgType e) {
        return R.ok(baseService.save(e));
    }

    @Put
    @Mapping("{id}")
    public R<?> update(FgType e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Put
    @Mapping("{id}/{dis}")
    public R<?> updateDis(Long id, Boolean dis) {
        return R.ok(baseService.lambdaUpdate().eq(BaseEntity::getId, id).set(FgType::getDis, dis).update());
    }

    @Post
    @Mapping("saveOrUpdate")
    public R<?> saveOrUpdate(FgType e) {
        return R.ok(baseService.saveOrUpdate(e));
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(Long id) {
        return R.ok(baseService.removeById(id));
    }

}
