package org.nism.fg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nism.fg.domain.dto.MapDTO;
import org.nism.fg.domain.entity.FgType;

import java.util.List;
import java.util.Map;

public interface FgTypeService extends IService<FgType> {

    MapDTO loadMap();

}
