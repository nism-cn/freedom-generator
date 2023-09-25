package org.nism.fg.controller;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.R;
import org.nism.fg.domain.entity.FgProjectSetting;
import org.nism.fg.service.FgProjectSettingService;
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
public class FgProjectSettingController {

    @Inject
    private FgProjectSettingService baseService;

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
    public R<?> create(FgProjectSetting e) {
        baseService.save(e);
        return R.ok(e);
    }

    @Put
    @Mapping("{id}")
    public R<?> update(FgProjectSetting e, Long id) {
        e.setId(id);
        return R.ok(baseService.updateById(e));
    }

    @Post
    @Mapping("saveOrUpdate")
    public R<?> saveOrUpdate(FgProjectSetting e) {
        baseService.saveOrUpdate(e);
        return R.ok(e);
    }

    @Delete
    @Mapping("{id}")
    public R<?> delete(String id) {
        return R.ok(baseService.removeById(id));
    }


}
