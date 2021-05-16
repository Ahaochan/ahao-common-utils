package moe.ahao.domain.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Ahaochan on 2017/7/18.
 * 传递Ajax的Data Transfer Object
 */
public class AjaxDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final int FAILURE = 1;
    public static final int SUCCESS = 0;

    private int result;
    private String msg;
    private Object obj;

    private AjaxDTO() {
    }

    private AjaxDTO(int result, String msg, Object obj) {
        this.result = result;
        this.msg = msg;
        this.obj = obj;
    }

    public static AjaxDTO failure() {
        return new AjaxDTO(FAILURE, "failure", null);
    }

    public static AjaxDTO failure(String msg) {
        return new AjaxDTO(FAILURE, msg, null);
    }

    public static AjaxDTO failure(Object obj) {
        return new AjaxDTO(FAILURE, "failure", obj);
    }

    public static AjaxDTO failure(String message, Object data) {
        return new AjaxDTO(FAILURE, message, data);
    }

    public static AjaxDTO success() {
        return new AjaxDTO(SUCCESS, "success", null);
    }

    public static AjaxDTO success(String msg) {
        return new AjaxDTO(SUCCESS, msg, null);
    }

    public static AjaxDTO success(Object obj) {
        return new AjaxDTO(SUCCESS, "success", obj);
    }

    public static AjaxDTO success(String message, Object data) {
        return new AjaxDTO(SUCCESS, message, data);
    }

    public static AjaxDTO get(boolean success) {
        return new AjaxDTO(success ? SUCCESS : FAILURE, success ? "success" : "error", null);
    }

    public static AjaxDTO get(int result, String message) {
        return new AjaxDTO(result, message, null);
    }

    public static AjaxDTO get(int result, String message, Object data) {
        return new AjaxDTO(result, message, data);
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AjaxDTO dto = (AjaxDTO) o;
        return result == dto.result &&
            Objects.equals(msg, dto.msg) &&
            Objects.equals(obj, dto.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, msg, obj);
    }

    @Override
    public String toString() {
        return "AjaxDTO{" +
            "result=" + result +
            ", msg='" + msg + '\'' +
            ", obj=" + obj +
            '}';
    }
}
