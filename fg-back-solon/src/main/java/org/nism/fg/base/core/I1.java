package org.nism.fg.base.core;

import lombok.Data;
import org.noear.solon.validation.annotation.NotNull;

/**
 * 带一个请求主体
 *
 * @author inism
 * @since 1.0.0
 */
@Data
public class I1<D> {

    /*** 数据 */
    @NotNull
    private D data;

}
