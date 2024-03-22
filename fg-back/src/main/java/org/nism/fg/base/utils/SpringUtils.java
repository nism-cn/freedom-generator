package org.nism.fg.base.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * spring工具类
 *
 * @author nism
 * @since 1.0.0
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {

    private SpringUtils() {
    }

    private static ConfigurableListableBeanFactory beanFactory;
    private static ApplicationContext context;

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return beanFactory.getBeansOfType(clazz);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringUtils.context = context;
    }

    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return beanFactory.getBean(beanName, clazz);
    }

    public static Object getBean(String beanName, Object... objects) {
        return beanFactory.getBean(beanName, objects);
    }

    public static <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, Object... objects) {
        return beanFactory.getBean(clazz, objects);
    }

    public static <T> ObjectProvider<T> getBeanProvider(Class<T> clazz) {
        return beanFactory.getBeanProvider(clazz);
    }

    public static <T> ObjectProvider<T> getBeanProvider(ResolvableType type) {
        return beanFactory.getBeanProvider(type);
    }


    public static boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }

    public static boolean isSingleton(String beanName) {
        return beanFactory.isSingleton(beanName);
    }

    public static boolean isPrototype(String beanName) {
        return beanFactory.isPrototype(beanName);
    }

    public static boolean isTypeMatch(String beanName, ResolvableType type) {
        return beanFactory.isTypeMatch(beanName, type);
    }

    public static boolean isTypeMatch(String beanName, Class<?> clazz) {
        return beanFactory.isTypeMatch(beanName, clazz);
    }

    @Nullable
    public static Class<?> getType(String beanName) {
        return beanFactory.getType(beanName);
    }

    @Nullable
    public static Class<?> getType(String beanName, boolean flag) {
        return beanFactory.getType(beanName, flag);
    }

    public static String[] getAliases(String beanName) {
        return beanFactory.getAliases(beanName);
    }


    public static boolean isActiveProfile(String profile) {
        ConfigurableEnvironment env = beanFactory.getBean(ConfigurableEnvironment.class);
        String[] activeProfiles = env.getActiveProfiles();
        if (activeProfiles.length <= 0 || !StringUtils.hasText(profile)) {
            return false;
        }
        Set<String> collect = Arrays.stream(activeProfiles).filter(profile::equals).collect(Collectors.toSet());
        return collect.size() > 0;
    }


}
