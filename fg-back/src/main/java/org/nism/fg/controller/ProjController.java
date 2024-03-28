package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.mvc.controller.BaseController;
import org.nism.fg.base.core.mvc.domain.I1;
import org.nism.fg.base.core.mvc.domain.R;
import org.nism.fg.domain.entity.Proj;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.service.ProjService;
import org.nism.fg.service.SetsService;
import org.nism.fg.service.TypeService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 项目控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("proj")
public class ProjController extends BaseController<ProjService, Proj> {

    private final ProjService projService;
    private final TypeService typeService;
    private final SetsService setsService;

    @GetMapping("setting/{id}")
    public R<?> findSetting(@PathVariable Serializable id) {
        Sets one = setsService.lambdaQuery().eq(Sets::getProjectId, id).one();
        return R.ok(Optional.ofNullable(one).orElse(new Sets()));
    }

    @PostMapping("importTables/{projectId}")
    public R<?> importTables(@PathVariable Serializable projectId, @RequestBody I1<List<String>> tables) {
        projService.importTables(projectId, tables.getData());
        return R.ok();
    }

    @GetMapping("types")
    private R<?> types() {
        return R.ok(typeService.loadMaps());
    }

}
