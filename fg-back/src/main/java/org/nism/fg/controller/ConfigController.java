package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.mvc.controller.BaseController;
import org.nism.fg.base.core.mvc.domain.R;
import org.nism.fg.base.utils.StringUtils;
import org.nism.fg.domain.entity.Config;
import org.nism.fg.service.ConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据源控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("config")
public class ConfigController extends BaseController<ConfigService, Config> {

    @GetMapping("/type/{type}")
    public R<?> findByType(@PathVariable String type, String search) {
        List<Config> list = baseService.lambdaQuery()
                .like(StringUtils.isNotBlank(search), Config::getName, search)
                .eq(Config::getType, type)
                .list();
        return R.ok(list);
    }

}
