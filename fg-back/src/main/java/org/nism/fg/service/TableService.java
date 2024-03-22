package org.nism.fg.service;

import cn.hutool.db.meta.Table;
import org.nism.fg.base.core.IService;
import org.nism.fg.domain.dto.DictDTO;
import org.nism.fg.domain.dto.PreviewDTO;
import org.nism.fg.domain.entity.FgTable;
import org.nism.fg.domain.entity.Column;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface TableService extends IService<FgTable> {

    @Override
    FgTable getById(Serializable id);

    List<FgTable> selectBatchIdsUnion(Collection<?> ids);

    void modify(FgTable table, List<Column> columns);

    @Override
    boolean removeById(Serializable id);

    @Override
    boolean removeByIds(Collection<?> ids);

    List<PreviewDTO> preview(Serializable id) throws Exception;

    byte[] generator(Collection<?> ids) throws Exception;

    List<Table> getDbTables(Serializable settingId);

    List<DictDTO> dictList(Serializable tableId);
}
