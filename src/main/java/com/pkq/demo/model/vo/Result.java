package com.pkq.demo.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private  int code;
    private  String msg;
    private  T data;

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功的快捷方法
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(200, "success", null);
    }

    // 失败的快捷方法
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}
