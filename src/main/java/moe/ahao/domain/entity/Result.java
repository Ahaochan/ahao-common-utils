package moe.ahao.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Ahaochan on 2017/7/18.
 * 传递Ajax的Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final int FAILURE = 1;
    public static final int SUCCESS = 0;

    private int code;
    private String msg;
    private T obj;

    public static <T> Result<T> failure() {
        return new Result<>(FAILURE, "failure", null);
    }

    public static <T> Result<T> failure(String msg) {
        return new Result<>(FAILURE, msg, null);
    }

    public static <T> Result<T> failure(T obj) {
        return new Result<>(FAILURE, "failure", obj);
    }

    public static <T> Result<T> failure(String message, T data) {
        return new Result<>(FAILURE, message, data);
    }

    public static <T> Result<T> success() {
        return new Result<>(SUCCESS, "success", null);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(SUCCESS, msg, null);
    }

    public static <T> Result<T> success(T obj) {
        return new Result<>(SUCCESS, "success", obj);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS, message, data);
    }

    public static <T> Result<T> get(boolean success) {
        return new Result<>(success ? SUCCESS : FAILURE, success ? "success" : "error", null);
    }

    public static <T> Result<T> get(int result, String message) {
        return new Result<>(result, message, null);
    }

    public static <T> Result<T> get(int result, String message, T data) {
        return new Result<>(result, message, data);
    }
}
