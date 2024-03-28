package org.nism.fg.base.config;

import cn.hutool.core.io.FileUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.nism.fg.base.utils.SystemUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.TimeZone;

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
    private final static String STR_UTIL = "cn.hutool.core.util.StrUtil";
    private final static String TOOLS = "org.nism.fg.base.utils.FtlTools";

    @Bean
    @Primary
    public Configuration cfg() throws IOException, TemplateModelException {
        Configuration cfg = new Configuration(VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(FileUtil.mkdir(SystemUtils.getTemplateDir()));
        commonInit(cfg);
        return cfg;
    }

    @Bean
    public Configuration stringCfg() throws TemplateModelException {
        Configuration cfg = new Configuration(VERSION_2_3_31);
        cfg.setTemplateLoader(new StringTemplateLoader());
        commonInit(cfg);
        return cfg;
    }

    private void commonInit(Configuration cfg) throws TemplateModelException {
        // 编码
        cfg.setDefaultEncoding("UTF-8");
        // 中国时间
        cfg.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setSharedVariable("strUtil", staticModels.get(STR_UTIL));
        cfg.setSharedVariable("tools", staticModels.get(TOOLS));
    }

}
