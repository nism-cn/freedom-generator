package org.nism.fg.service;

import cn.hutool.db.meta.Table;
import com.baomidou.mybatisplus.solon.service.IService;
import org.nism.fg.domain.dto.DictDTO;
import org.nism.fg.domain.dto.PreviewDTO;
import org.nism.fg.domain.entity.FgTable;
import org.nism.fg.domain.entity.FgTableColumn;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface FgTableService extends IService<FgTable> {

    @Override
    FgTable getById(Serializable id);

    List<FgTable> selectBatchIdsUnion(Collection<?> ids);

    void modify(FgTable table, List<FgTableColumn> columns);

    @Override
    boolean removeById(Serializable id);

    @Override
    boolean removeByIds(Collection<?> ids);

    List<PreviewDTO> preview(Long id) throws Exception;

    byte[] generator(Collection<?> ids) throws Exception;

    List<Table> getDbTables(Long settingId);

    List<DictDTO> dictList(Long tableId);
}
