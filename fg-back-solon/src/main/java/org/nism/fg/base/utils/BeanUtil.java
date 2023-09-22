package org.nism.fg.base.utils;

import org.noear.solon.Solon;

import java.util.List;

public class BeanUtil {


    public static <T> T getBean(String beanName, Class<T> clazz) {
        List<T> beansOfType = Solon.context().getBeansOfType(clazz);
        return beansOfType.get(0);
    }


}
