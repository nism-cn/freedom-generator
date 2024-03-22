package org.nism.fg.controller;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.service.SetsService;
import org.noear.solon.annotation.*;

/**
 * 项目设置控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@Controller
@Mapping("project-setting")
public class SetsController {

    @Inject
    private SetsService baseService;

    @Get
    @Mapping
    public R<?> find() {
        return R.ok(baseService.list());
    }

    @Get
    @Mapping("{id}")
    public R<?> findOne(Long id) {
        return R.ok(baseService.getById(id));
    }

    @Post
    @Mapping
    public R<?> create(Sets e) {
        baseService.save(e);
        return R.ok(e);
    }

    @Put
    @Mapping("{id}")
    public R<?> update(Sets e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Post
    @Mapping("saveOrUpdate")
    public R<?> saveOrUpdate(Sets e) {
        baseService.saveOrUpdate(e);
        return R.ok(e);
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(String id) {
        return R.ok(baseService.removeById(id));
    }


}
