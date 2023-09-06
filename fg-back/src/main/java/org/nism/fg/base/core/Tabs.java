package org.nism.fg.base.core;

import java.io.Serializable;

/**
 * 前端tab页签标记
 *
 * @author inism
 * @since 1.0.0
 */
public interface Tabs {

    /**
     * tab 主键
     *
     * @return 主键
     */
    Serializable getId();

    /**
     * tab 显示名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 属于模块
     *
     * @return 模块
     */
    String getMainType();

}
