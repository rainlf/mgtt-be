package com.rainlf.weixinmpserver.controller.model;

import lombok.Data;

/**
 * @author rain
 * @date 5/30/2024 10:49 AM
 */
@Data
public class ApiResp<T> {
    public boolean success;
    public T data;
    public String msg;

    public static <T> ApiResp<T> success(T data) {
        ApiResp<T> apiResp = new ApiResp<>();
        apiResp.success = true;
        apiResp.data = data;
        return apiResp;
    }

    public static <T> ApiResp<T> success() {
        ApiResp<T> apiResp = new ApiResp<>();
        apiResp.success = true;
        return apiResp;
    }

    public static <T> ApiResp<T> fail(String msg) {
        ApiResp<T> apiResp = new ApiResp<>();
        apiResp.success = false;
        apiResp.msg = msg;
        return apiResp;
    }
}
