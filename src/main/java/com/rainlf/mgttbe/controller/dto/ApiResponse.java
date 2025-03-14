package com.rainlf.mgttbe.controller.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    private ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // 成功响应（带数据）
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "Operation successful");
    }

    // 成功响应（带自定义消息）
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }

    // 成功响应（无数据）
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, "Operation successful");
    }

    // 失败响应（带错误消息）
    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, null, message);
    }

    // 失败响应（带详细错误数据）
    public static <T> ApiResponse<T> failure(T errorData, String message) {
        return new ApiResponse<>(false, errorData, message);
    }
}
