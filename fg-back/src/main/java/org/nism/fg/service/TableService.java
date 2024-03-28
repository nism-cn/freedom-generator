package org.nism.fg.service;

import org.nism.fg.base.core.mvc.service.IService;
import org.nism.fg.domain.dto.DictDTO;
import org.nism.fg.domain.dto.PreviewDTO;
import org.nism.fg.domain.entity.Column;
import org.nism.fg.domain.entity.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface TableService extends IService<Table> {

    @Override
    Table getById(Serializable id);

    List<Table> selectBatchIdsUnion(Collection<?> ids);

    Table selectByIdUnion(Serializable id);

    void modify(Table table, List<Column> columns);

    @Override
    boolean removeById(Serializable id);

    @Override
    boolean removeByIds(Collection<?> ids);

    List<PreviewDTO> preview(Serializable id) throws Exception;

    byte[] generator(Collection<?> ids) throws Exception;

    List<cn.hutool.db.meta.Table> getDbTables(Serializable setsId);

    List<DictDTO> dictList(Serializable tableId);
}
