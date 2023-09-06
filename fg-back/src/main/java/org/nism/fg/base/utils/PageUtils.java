package org.nism.fg.base.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import javax.servlet.http.HttpServletRequest;
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
        HttpServletRequest request = ServletUtils.getRequest();
        int pageNum = Integer.parseInt(Optional.ofNullable(request.getParameter("pageNum")).orElse("1"));
        int pageSize = Integer.parseInt(Optional.ofNullable(request.getParameter("pageSize")).orElse("10"));
        PageHelper.startPage(pageNum, pageSize);
        return PageHelper.startPage(pageNum, pageSize);
    }

    /**
     * 物理分页
     *
     * @return 分页对象
     */
    public static Page<Object> startPage(List<?> data) {
        HttpServletRequest request = ServletUtils.getRequest();
        int pageNum = Integer.parseInt(Optional.ofNullable(request.getParameter("pageNum")).orElse("1"));
        int pageSize = Integer.parseInt(Optional.ofNullable(request.getParameter("pageSize")).orElse("10"));
        PageHelper.startPage(pageNum, pageSize);
        return PageHelper.startPage(pageNum, pageSize);
    }

}
