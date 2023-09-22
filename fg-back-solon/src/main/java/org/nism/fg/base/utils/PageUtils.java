package org.nism.fg.base.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.noear.solon.core.handle.Context;

import java.util.List;
import java.util.Optional;

/**
 * 分页工具
 *
 * @author inism
 * @since 1.0.0
 */
public class PageUtils {

    private PageUtils() {
    }

    /**
     * 数据库分页
     *
     * @return 分页对象
     */
    public static Page<Object> startPage() {
        Context ctx = Context.current();
        int pageNum = Integer.parseInt(Optional.ofNullable(ctx.param("pageNum")).orElse("1"));
        int pageSize = Integer.parseInt(Optional.ofNullable(ctx.param("pageSize")).orElse("10"));
        PageHelper.startPage(pageNum, pageSize);
        return PageHelper.startPage(pageNum, pageSize);
    }

    /**
     * 物理分页
     *
     * @return 分页对象
     */
    public static Page<Object> startPage(List<?> data) {
        Context ctx = Context.current();
        int pageNum = Integer.parseInt(Optional.ofNullable(ctx.param("pageNum")).orElse("1"));
        int pageSize = Integer.parseInt(Optional.ofNullable(ctx.param("pageSize")).orElse("10"));
        PageHelper.startPage(pageNum, pageSize);
        return PageHelper.startPage(pageNum, pageSize);
    }

}
