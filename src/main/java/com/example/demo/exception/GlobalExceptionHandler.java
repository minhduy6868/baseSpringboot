package com.example.demo.exception;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.demo.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //hàm này giúp xử lí lỗi ko xác định, là hằng số code 999
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleRuntimeException(AppException e) {  //giúp hàm () -> new RuntimeException("User not found!") chạy
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.EXCEPTIONN_CATCH.getCode());
        apiResponse.setMessage(ErrorCode.EXCEPTIONN_CATCH.getMessage());
        ErrorCode err = e.getErrorCode(); //refactor lại từ AppException

//        apiResponse.setCode(err.getCode());
//        apiResponse.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .data(errorCode.getMessage())
                        .build()
        );
    }

    //hàm handle Exception theo AppException và mã lỗi trong ErrorCode
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode err = e.getErrorCode();

        apiResponse.setCode(err.getCode());
        apiResponse.setMessage(err.getMessage());

        return ResponseEntity
                .status(err.getStatusCode())
                .body(apiResponse);
    }

    //hàm bắt lỗi validete
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse apiResponse = new ApiResponse();
        String enumKey = e.getFieldError().getDefaultMessage(); //Lấy content lỗi chi tiết

        ErrorCode errorCode = ErrorCode.INVALID_VALID_KEY; //mặc định là mỗi sai key

        try {
             errorCode = ErrorCode.valueOf(enumKey); //check xem có giá trị không, nếu sai key trả về mã lỗi mặc định sai key
        }catch (IllegalArgumentException em) {

        }

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse); //in lỗi cụ thể khi xài valid
    }

}
