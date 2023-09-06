package org.nism.fg.base.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet工具类
 *
 * @author inism
 * @since 1.0.0
 */
public class ServletUtils {

    private ServletUtils() {
    }

    /**
     * 获取请求对象
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取相应
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static void byteDownload(byte[] data) throws IOException {
        HttpServletResponse response = getResponse();
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String now = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMATTER);
        String fileName = "FG" + now + ".zip";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), true, data);
    }

    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

}
