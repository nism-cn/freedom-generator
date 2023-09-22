package org.nism.fg.domain.convert;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.nism.fg.base.utils.SystemUtils;
import org.nism.fg.domain.dto.FileDTO;

import java.io.File;

public class FileConvert {

    public static FileDTO to(File file) {
        String pathString = file.toString().replace("\\", "/");
        // 相对子路径
        String relativePath = StrUtil.removePrefix(pathString, SystemUtils.getTemplateDir());
        // 父路径
        String parentPath = StrUtil.subBefore(relativePath, "/", true);
        // 当前路径
        String currentPath = StrUtil.subAfter(relativePath, "/", true);

        FileDTO dto = new FileDTO();
        dto.setProject(StrUtil.isEmpty(currentPath));
        dto.setDirectory(file.isDirectory());
        dto.setFile(file.isFile());
        dto.setRelativePath(relativePath);
        dto.setAbsolutePath(pathString);
        dto.setCurrentPath(currentPath);
        dto.setName(FileUtil.getPrefix(file));
        dto.setSuffix(FileUtil.getSuffix(currentPath));
        dto.setParentPath(parentPath);
        return dto;
    }

}
