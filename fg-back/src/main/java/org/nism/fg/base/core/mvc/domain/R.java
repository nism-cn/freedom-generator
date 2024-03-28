package org.nism.fg.base.core.mvc.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 相应主体
 *
 * @author inism
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class R<T> implements Serializable {

    /*** 成功 */
    public static final int OK = 200;
    /*** 失败 */
    public static final int FAIL = 500;

    private int code;

    private String msg;

    private T data;

    private Page page;

    public static <T> R<T> msg(String msg) {
        return restResult(null, OK, msg);
    }

    public static <T> R<T> ok() {
        return restResult(null, OK, "操作成功");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, OK, "操作成功");
    }

    public static <T> R<T> ok(String msg, T data) {
        return restResult(data, OK, msg);
    }

    public static <T> R<T> fail() {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data) {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg, T data) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    public static <T> R<T> warn(String msg) {
        return restResult(null, 601, msg);
    }

    public static <T> R<T> warn(String msg, T data) {
        return restResult(data, 601, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static <T> Boolean isError(R<T> ret) {
        return !isOk(ret);
    }

    public static <T> Boolean isOk(R<T> ret) {
        return R.OK == ret.getCode();
    }

    public boolean getOk() {
        return R.OK == this.code;
    }

    @Data
    public static class Page implements Serializable {
        private Integer total;
        private Integer pageNum;
        private Integer pageSize;
        private List<?> rows;
    }

}
