package org.nism.fg.base.core.mvc.service;

import org.nism.fg.base.core.mvc.mapper.BaseMapper;

public class ServiceImpl<M extends BaseMapper<T>, T> extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M, T> implements IService<T> {
}
