package org.nism.fg.base.config;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.nism.fg.base.freemarker.*;
import org.nism.fg.base.utils.SystemUtils;
import org.noear.solon.annotation.Bean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板引擎配置
 *
 * @author inism
 * @since 1.0.0
 */
@org.noear.solon.annotation.Configuration
public class FreemarkerConfig {

    @Bean
    public Configuration cfg() throws IOException, TemplateModelException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(FileUtil.mkdir(SystemUtils.getTemplateDir()));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Map<String, TemplateModel> strUtil = new HashMap<>();
        strUtil.put("toUnderline", new ToUnderlineCaseMethodModel());
        strUtil.put("toCame", new ToCameCaseMethodModel());
        strUtil.put("toPascal", new ToPascalCaseCaseMethodModel());
        strUtil.put("lowerFirst", new LowerFirstMethodModel());
        strUtil.put("upperFirst", new UpperFirstMethodModel());

        cfg.setSharedVariable("strUtil", strUtil);
        return cfg;
    }

}
