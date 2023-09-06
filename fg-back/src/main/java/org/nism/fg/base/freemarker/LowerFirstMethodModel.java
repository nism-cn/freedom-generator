package org.nism.fg.base.freemarker;

import cn.hutool.core.util.StrUtil;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * Freemarker 提供内置函数
 * 小写首字母
 *
 * @author inism
 * @since 1.0.0
 */
public class LowerFirstMethodModel implements TemplateMethodModelEx {

    /**
     * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
     * 例如：
     *
     * <pre>
     * HelloWorld=》hello_world
     * Hello_World=》hello_world
     * HelloWorld_test=》hello_world_test
     * </pre>
     *
     * @return 转换后下划线方式命名的字符串
     */
    @Override
    public TemplateModel exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Wrong arguments");
        }
        return new SimpleScalar(StrUtil.lowerFirst(args.get(0).toString()));
    }
}