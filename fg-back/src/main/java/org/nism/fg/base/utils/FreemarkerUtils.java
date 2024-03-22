package org.nism.fg.base.utils;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.nism.fg.base.constant.CoreConstant;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class FreemarkerUtils {

    private FreemarkerUtils() {
    }


    public static String getStringVal(Environment env, String key) {
        try {
            TemplateModel defOutPath = env.getVariable(key);
            if (null != defOutPath) {
                if (defOutPath instanceof SimpleScalar) {
                    return ((SimpleScalar) defOutPath).getAsString();
                } else {
                    throw new IllegalArgumentException("输出路径参数不正确");
                }
            }
        } catch (TemplateModelException e) {
            throw new IllegalArgumentException("输出路径参数不正确", e);
        }
        return null;
    }

}
