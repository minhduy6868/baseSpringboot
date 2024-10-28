package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXIST(404, "User already exists", HttpStatus.NOT_FOUND),
    EXCEPTIONN_CATCH(999, "KHông xác định được lỗi!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_VALID_KEY(1001, "Không tìm thấy mã lỗi valid", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Mật khẩu phải lớn hơn 8 kí tự", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4004, "Không tìm ra người dùng này", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATION(4005, "Không thể xác thực người dùng", HttpStatus.UNAUTHORIZED),
    ERR_PASSWORD(1007, "Mật khẩu lỗi", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1008, "Không có quyền truy cập", HttpStatus.FORBIDDEN);





    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
