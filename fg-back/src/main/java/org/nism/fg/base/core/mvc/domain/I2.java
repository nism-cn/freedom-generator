package org.nism.fg.base.core.mvc.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 带二个请求主体
 *
 * @author inism
 * @since 1.0.0
 */
@Data
public class I2<D, S> {

    /*** 数据 */
    @NotNull
    private D data;
    /*** 子数据 */
    @NotNull
    private S sub;

}
