package org.nism.fg.base.utils;

import freemarker.core.Environment;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

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
                    throw new IllegalArgumentException(key + "参数不正确");
                }
            }
        } catch (TemplateModelException e) {
            throw new IllegalArgumentException(key + "参数不正确", e);
        }
        return null;
    }

    public static Number getNumberVal(Environment env, String key) {
        try {
            TemplateModel defShowIndex = env.getVariable(key);
            if (null != defShowIndex) {
                if (defShowIndex instanceof SimpleNumber) {
                    return ((SimpleNumber) defShowIndex).getAsNumber();
                } else {
                    throw new IllegalArgumentException(key + "参数不正确");
                }
            }
        } catch (TemplateModelException e) {
            throw new IllegalArgumentException(key + "参数不正确", e);
        }
        return 0;
    }

}
