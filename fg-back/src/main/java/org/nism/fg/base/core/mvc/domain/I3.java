package org.nism.fg.base.core.mvc.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 带三个请求主体
 *
 * @author inism
 * @since 1.0.0
 */
@Data
public class I3<D, C, S> {

    /*** 数据 */
    @NotNull
    private D data;
    /*** 子数据 */
    @NotNull
    private C sub;
    /*** 孙数据 */
    @NotNull
    private S sun;

}
