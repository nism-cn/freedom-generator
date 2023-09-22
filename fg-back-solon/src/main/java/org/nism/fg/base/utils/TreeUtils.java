package org.nism.fg.base.utils;

import org.nism.fg.base.core.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 树操作工具
 *
 * @author nism
 * @since 1.0.0
 */
public class TreeUtils {

    private TreeUtils() {
    }

    /**
     * 数据通过递归整理为树结构
     *
     * @param list   源数据
     * @param parent 根节点
     * @param <E>    树类型
     * @return 树数据
     */
    public static <E extends Tree<E>> List<E> buildTree(List<E> list, String parent) {
        List<E> tree = new ArrayList<>();
        list.forEach(e -> {
            if (e.getParent().equals(parent)) {
                e.setChildren(buildTree(list, e.getChildrenId()));
                tree.add(e);
            }
        });
        return tree;
    }

}
