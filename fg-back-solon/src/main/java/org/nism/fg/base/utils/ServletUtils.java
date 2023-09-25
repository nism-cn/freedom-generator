package org.nism.fg.base.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.DownloadedFile;

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

    public static void byteDownload(byte[] data) throws IOException {
        Context ctx = Context.current();
        String contentType = "application/octet-stream; charset=UTF-8";
        String fileName = "FG" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMATTER) + ".zip";
        DownloadedFile file = new DownloadedFile(contentType, data, fileName);
        ctx.outputAsFile(file);
    }

    private static Context getCtx() {
        return Context.current();
    }

}
