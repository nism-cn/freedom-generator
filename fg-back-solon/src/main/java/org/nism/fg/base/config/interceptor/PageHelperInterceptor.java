package org.nism.fg.base.config.interceptor;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.R;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.core.route.RouterInterceptor;
import org.noear.solon.core.route.RouterInterceptorChain;

/**
 * 自处理分页
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
@Component
public class PageHelperInterceptor implements RouterInterceptor {

    @Override
    public void doIntercept(Context ctx, Handler mainHandler, RouterInterceptorChain chain) throws Throwable {
        chain.doIntercept(ctx, mainHandler);
    }

    @Override
    public Object postResult(Context ctx, Object body) throws Throwable {
        if (body instanceof R) {
            R<?> r = (R<?>) body;
            Object data = r.getData();
            if (data instanceof Page) {
                Page<?> page = (Page<?>) data;
                R.Page p = new R.Page();
                p.setTotal((int) page.getTotal());
                p.setPageNum(page.getPageNum());
                p.setPageSize(page.getPageSize());
                p.setRows(page.getResult());
                r.setPage(p);
                r.setData(null);
                log.debug("***** PageHelper.clearPage() *****");
                PageHelper.clearPage();
            }
        }
        return body;
    }

}
