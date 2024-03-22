package org.nism.fg.base.config.filer;

import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.core.R;
import org.nism.fg.base.utils.JsonUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;

@Slf4j
@Component
public class AppFilter implements Filter {

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        if ("/".equals(ctx.pathNew())) {
            ctx.pathNew("/index.html");
        }
        try {
            chain.doFilter(ctx);
        } catch (Exception e) {
            ctx.status(200);
            ctx.outputAsJson(JsonUtils.toString(R.fail(e.getMessage())));
            log.error("exception", e);
        }
    }
}