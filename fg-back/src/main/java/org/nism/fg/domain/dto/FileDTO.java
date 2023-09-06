package org.nism.fg.domain.dto;

import lombok.Data;
import org.nism.fg.base.core.Tabs;
import org.nism.fg.base.core.Tree;
import org.nism.fg.domain.enums.TabsType;

import java.io.Serializable;
import java.util.List;

/**
 * 文件数据传输对象
 *
 * @author nism
 * @since 1.0.0
 */
@Data
public class FileDTO implements Tree<FileDTO>, Tabs, Serializable {

    /*** 自己的名字的父路径 */
    private String parentPath;
    /*** 文件名/文件夹名 - 自己的名字 */
    private String currentPath;
    /*** 文件名 */
    private String name;
    /*** 文件尾缀 */
    private String suffix;
    /*** 相对[temp]路径 */
    private String relativePath;
    /*** 磁盘绝对路径 */
    private String absolutePath;
    /*** 是否为目录 */
    private Boolean directory;
    /*** 是否为文件 */
    private Boolean file;
    /*** 是否为根 */
    private Boolean project;
    /*** 是否模板组 */
    private Boolean group;
    /*** 相对父路径 */
    private List<FileDTO> children;

    @Override
    public String getParent() {
        return parentPath;
    }

    @Override
    public String getChildrenId() {
        return relativePath;
    }

    @Override
    public String getId() {
        return relativePath;
    }

    @Override
    public String getMainType() {
        return TabsType.template.name();
    }
}
