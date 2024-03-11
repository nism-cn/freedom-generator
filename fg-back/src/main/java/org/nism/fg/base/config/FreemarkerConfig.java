package org.nism.fg.base.config;

import cn.hutool.core.io.FileUtil;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.*;
import org.nism.fg.base.utils.SystemUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

import static freemarker.template.Configuration.VERSION_2_3_31;

/**
 * 模板引擎配置
 *
 * @author inism
 * @since 1.0.0
 */
@org.springframework.context.annotation.Configuration
public class FreemarkerConfig {

    private final static TemplateHashModel staticModels = new BeansWrapperBuilder(VERSION_2_3_31).build().getStaticModels();

    @Bean
    @Primary
    public Configuration cfg() throws IOException, TemplateModelException {
        Configuration cfg = new Configuration(VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(FileUtil.mkdir(SystemUtils.getTemplateDir()));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        TemplateModel strUtil = staticModels.get("cn.hutool.core.util.StrUtil");
        cfg.setSharedVariable("strUtil", strUtil);
        return cfg;
    }

}
