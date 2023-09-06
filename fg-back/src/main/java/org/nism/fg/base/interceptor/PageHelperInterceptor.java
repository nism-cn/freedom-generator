package org.nism.fg.base.interceptor;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.constant.CoreConstant;
import org.nism.fg.base.core.R;
import org.nism.fg.base.utils.SpringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 自处理分页
 *
 * @author inism
 * @since 1.0.0
 */
@Slf4j
@ControllerAdvice
public class PageHelperInterceptor implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter parameter, Class<? extends HttpMessageConverter<?>> clazz) {
        return true;
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter parameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> clazz, ServerHttpRequest request, ServerHttpResponse response) {
        //此处的 Result 对象是我自定义的返回值类型,具体根据自己需求修改即可
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
