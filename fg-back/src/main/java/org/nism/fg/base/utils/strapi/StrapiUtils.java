package org.nism.fg.base.utils.strapi;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.nism.fg.base.utils.JsonUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.nism.fg.base.utils.strapi.Constants.$BETWEEN;
import static org.nism.fg.base.utils.strapi.Constants.DAY_FMT;


@Slf4j
public class StrapiUtils {

    private StrapiUtils() {
    }

    public static Object getBeginTime(Object o) {
        if (isFrontNull(o)) {
            return null;
        }
        Object v1 = getDateString(o).get(0);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DAY_FMT);
            return sdf.parse(v1.toString());
        } catch (Exception e) {
            log.error("{}.startTime: {}", $BETWEEN, e.getMessage());
            return v1;
        }
    }

    public static Object getEndTime(Object o) {
        if (isFrontNull(o)) {
            return null;
        }
        Object v2 = getDateString(o).get(1);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DAY_FMT);
            Date parse = sdf.parse(v2.toString());
            return new Date(parse.getTime() + 24 * 60 * 60 * 1000 - 1);
        } catch (Exception e) {
            log.error("{}.endTime: {}", $BETWEEN, e.getMessage());
            return v2;
        }
    }

    public static List<Object> getDateString(Object o) {
        try {
            String s = o.toString().replace("'", "\"");
            return JsonUtils.toObject(s, new TypeReference<List<Object>>() {
            });
        } catch (Exception e) {
            log.error("传入参数不正确 {}", e.getMessage());
            return Arrays.asList(null, null);
        }
    }

    public static List<Object> getList(Object o) {
        if (isFrontNull(o)) {
            return null;
        }
        try {
            return JsonUtils.toObject(o.toString(), new TypeReference<List<Object>>() {
            });
        } catch (Exception e) {
            log.error("传入参数不正确 {}", e.getMessage());
            return null;
        }
    }

    public static boolean isFrontNull(Object obj) {
        return obj == null
                || obj.toString().isEmpty()
                || obj.toString().equalsIgnoreCase("null")
                || obj.toString().equalsIgnoreCase("undefined")
                || obj.toString().equalsIgnoreCase("[\"\"]");
    }

    public static boolean isNotFrontNull(Object obj) {
        return !isFrontNull(obj);
    }
}
