package org.nism.fg.base.core;

import com.baomidou.mybatisplus.extension.toolkit.Db;

import java.util.Collection;

/**
 * Mapper基类
 *
 * @author inism
 * @since 1.0.0
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    /**
     * 批量插入
     *
     * @param entityList e
     * @return 是否成功
     */
    default boolean insertBatch(Collection<T> entityList) {
        return Db.saveBatch(entityList);
    }

    /**
     * 批量更新
     *
     * @param entityList e
     * @return 是否成功
     */
    default boolean updateBatch(Collection<T> entityList) {
        return Db.updateBatchById(entityList);
    }

}
