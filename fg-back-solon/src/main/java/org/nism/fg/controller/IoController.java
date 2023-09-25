package org.nism.fg.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import org.nism.fg.base.core.I1;
import org.nism.fg.base.core.I2;
import org.nism.fg.base.core.R;
import org.nism.fg.base.utils.Assert;
import org.nism.fg.base.utils.SystemUtils;
import org.nism.fg.base.utils.TreeUtils;
import org.nism.fg.domain.convert.FileConvert;
import org.nism.fg.domain.dto.FileDTO;
import org.noear.solon.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件控制层
 *
 * @author nism
 * @since 1.0.0
 */
@Controller
@Mapping("io")
public class IoController {

    private static final String TEMP_PATH = SystemUtils.getTemplateDir();
    private static final String LOGS_PATH = SystemUtils.getLogsDir();

    /**
     * 获取项目结构
     */
    @Get
    @Mapping("file-tree")
    public R<?> fileTree() throws IOException {
        List<Path> walk = Files.walk(Paths.get(TEMP_PATH)).collect(Collectors.toList());
        List<FileDTO> fileTree = new ArrayList<>();
        for (Path path : walk) {
            FileDTO dto = FileConvert.to(path.toFile());
            if (StrUtil.equals(TEMP_PATH, dto.getAbsolutePath())) {
                continue;
            }
            fileTree.add(dto);
        }
        List<FileDTO> dtos = TreeUtils.buildTree(fileTree, "");
        dtos.forEach(e -> e.setGroup(true));
        return R.ok(dtos);
    }

    @Get
    @Mapping("temp")
    public R<?> temp() {
        List<FileDTO> tempList = new ArrayList<>();
        File[] ls = FileUtil.ls(TEMP_PATH);
        Assert.notEmpty(ls, "暂无模版");
        Arrays.stream(ls).forEach(e -> tempList.add(FileConvert.to(e)));
        return R.ok(tempList);
    }

    /**
     * 获取模版样例
     */
    @Get
    @Mapping("temp-demo")
    public R<?> tempDemo() {
        FileUtil.copy(ResourceUtil.getResource("demo").getPath(), TEMP_PATH, true);
        return R.ok();
    }

    /**
     * 创建目录
     */
    @Post
    @Mapping("mkdir")
    public R<?> mkdir(I1<String> path) {
        return R.ok(FileUtil.mkdir(TEMP_PATH + SystemUtils.SEP + path.getData()));
    }

    /**
     * 创建文件
     */
    @Post
    @Mapping("touch")
    public R<?> touch(I1<String> path) {
        return R.ok(FileUtil.touch(TEMP_PATH + SystemUtils.SEP + path.getData()));
    }

    /**
     * 删除文件
     */
    @Delete
    @Mapping("del")
    public R<?> del(I1<String> path) {
        return R.ok(FileUtil.del(TEMP_PATH + SystemUtils.SEP + path.getData()));
    }

    /**
     * 重命名
     */
    @Post
    @Mapping("rename")
    public R<?> rename(I2<String, String> data) {
        return R.ok(FileUtil.rename(new File(TEMP_PATH + SystemUtils.SEP + data.getData()), data.getSub(), true));
    }

    /**
     * 读取文件
     */
    @Post
    @Mapping("read")
    public R<?> read(I1<String> path) {
        String readPath = TEMP_PATH + SystemUtils.SEP + path.getData();
        String strings = FileUtil.readUtf8String(readPath);
        return R.ok(strings);
    }

    /**
     * 修改文件
     */
    @Post
    @Mapping("write")
    public R<?> write(I2<String, String> data) {
        String readPath = TEMP_PATH + SystemUtils.SEP + data.getData();
        FileUtil.writeUtf8String(data.getSub(), readPath);
        return R.ok();
    }

    /**
     * 读取日志
     */
    @Get
    @Mapping("log/{level}")
    public R<?> logs(String level) {
        String readPath = LOGS_PATH + SystemUtils.SEP + level + ".log";
        String strings = FileUtil.readUtf8String(readPath);
        return R.ok(strings);
    }

    /**
     * 清除日志
     */
    @Delete
    @Mapping("log/{level}")
    public R<?> clearLog(String level) {
        String readPath = LOGS_PATH + SystemUtils.SEP + level + ".log";
        FileUtil.writeUtf8String("", readPath);
        return R.ok();
    }

}
