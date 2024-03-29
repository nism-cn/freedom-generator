package org.nism.fg.base.core.mvc.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nism.fg.base.core.mvc.domain.BaseEntity;
import org.nism.fg.base.core.mvc.mapper.BaseMapper;
import org.nism.fg.base.utils.StringUtils;
import org.nism.fg.base.utils.strapi.StrapiUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.nism.fg.base.utils.strapi.Constants.*;

public class ServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M, T> implements IService<T> {

    @Override
    public List<T> find(T e) {
        return baseMapper.selectList(this.strapiWrapper(e));
    }

    @Override
    public Wrapper<T> strapiWrapper(T e) {
        QueryWrapper<T> ew = Wrappers.query();
        if (e.getFilters() != null) {
            for (Map.Entry<String, Map<String, Object>> entry : e.getFilters().entrySet()) {
                String field = entry.getKey();
                field = StringUtils.toUnderlineCase(field);
                Map<String, Object> condition = entry.getValue();

                Object eq = condition.get($EQ);
                ew.eq(StrapiUtils.isNotFrontNull(eq), field, eq);

                Object ne = condition.get($NE);
                ew.ne(StrapiUtils.isNotFrontNull(ne), field, ne);

                Object lt = condition.get($LT);
                ew.lt(StrapiUtils.isNotFrontNull(lt), field, lt);

                Object lte = condition.get($LTE);
                ew.le(StrapiUtils.isNotFrontNull(lte), field, lte);

                Object gt = condition.get($GT);
                ew.gt(StrapiUtils.isNotFrontNull(gt), field, gt);

                Object gte = condition.get($GTE);
                ew.ge(StrapiUtils.isNotFrontNull(gte), field, gte);

                Object in = StrapiUtils.getList(condition.get($IN));
                ew.in(StrapiUtils.isNotFrontNull(in), field, in);

                Object nul = condition.get($NULL);
                ew.isNull(StrapiUtils.isNotFrontNull(nul), field);

                Object notnull = condition.get($NOTNULL);
                ew.isNotNull(StrapiUtils.isNotFrontNull(notnull), field);

                Object like = condition.get($LIKE);
                ew.like(StrapiUtils.isNotFrontNull(like), field, condition.get($LIKE));

                Object between = condition.get($BETWEEN);
                Object beginTime = StrapiUtils.getBeginTime(between);
                Object endTime = StrapiUtils.getEndTime(between);
                ew.between(StrapiUtils.isNotFrontNull(between), field, beginTime, endTime);
            }
        }

        if (e.getSorts() != null) {
            List<String> ascList = new ArrayList<>();
            List<String> descList = new ArrayList<>();
            for (String sort : e.getSorts()) {
                List<String> split = StringUtils.split(sort, ":");
                Assert.isTrue(split.size() == 2, "xx:desc/asc Format Error!");
                String field = StringUtils.toUnderlineCase(split.get(0));
                String ordered = split.get(1).toLowerCase();
                // 兼容 element-ui 传参
                if ("asc".equals(ordered) || "ascending".equals(ordered)) {
                    ascList.add(field);
                } else if ("desc".equals(ordered) || "descending".equals(ordered)) {
                    descList.add(field);
                } else {
                    throw new IllegalArgumentException("asc/desc Spell Error!");
                }
            }
            ew.orderByAsc(!ascList.isEmpty(), ascList);
            ew.orderByDesc(!descList.isEmpty(), descList);
        }

        if (e.getFields() != null) {
            List<String> fields = e.getFields().stream().map(StringUtils::toUnderlineCase).collect(Collectors.toList());
            ew.select(!fields.isEmpty(), fields);
        }

        return ew;
    }

}
