package org.nism.fg.base.core.mvc.service;

import org.nism.fg.base.core.mvc.domain.BaseEntity;

public interface IService<T extends BaseEntity> extends com.baomidou.mybatisplus.extension.service.IService<T>, IStrapiService<T> {

}
