package org.nism.fg.base.utils;

import org.nism.fg.base.core.Tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param list       源数据
     * @param parent     根节点
     * @param comparator 排序
     * @param <E>        树类型
     * @return 树数据
     */
    public static <E extends Tree<E>> List<E> buildTree(List<E> list, String parent, Comparator<E> comparator) {
        List<E> tree = new ArrayList<>();

        // 进行排序
        List<E> currentLevelNodes = list.stream()
                .filter(e -> e.getParent().equals(parent))
                .sorted(comparator)
                .collect(Collectors.toList());

        // 对当前层级的节点进行排序
        for (E node : currentLevelNodes) {
            node.setChildren(buildTree(list, node.getChildrenId(), comparator));
            tree.add(node);
        }
        return tree;
    }

}
