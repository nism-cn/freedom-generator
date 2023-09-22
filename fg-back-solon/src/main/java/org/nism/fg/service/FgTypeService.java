package org.nism.fg.service;

import com.baomidou.mybatisplus.solon.service.IService;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.FgType;

public interface FgTypeService extends IService<FgType> {

    MapDTO loadMap();

}
