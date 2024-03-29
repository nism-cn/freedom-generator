package org.nism.fg.base.core.mvc.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.nism.fg.base.core.mvc.domain.BaseEntity;

import java.util.List;

public interface IStrapiService<T extends BaseEntity> {

    List<T> find(T e);

    Wrapper<T> strapiWrapper(T e);

}
