package org.nism.fg.base.core;

import java.util.List;

/**
 * 树结构抽象
 *
 * @author inism
 * @since 1.0.0
 */
public interface Tree<E> {

    /**
     * 父节点key
     *
     * @return key
     */
    String getParent();

    /**
     * 子点key
     *
     * @return key
     */
    String getChildrenId();

    /**
     * 设置子节点
     *
     * @param e node
     */
    void setChildren(List<E> e);

}
