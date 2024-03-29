package org.nism.fg.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.mvc.controller.BaseController;
import org.nism.fg.domain.entity.Sets;
import org.nism.fg.service.SetsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目设置控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("sets")
public class SetsController extends BaseController<SetsService, Sets> {

}
