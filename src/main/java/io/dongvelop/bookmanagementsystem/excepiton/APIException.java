package io.dongvelop.bookmanagementsystem.excepiton;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 커스텀 예외 클래스
 */
@Getter
public class APIException extends Exception {

    @Serial
    private static final long serialVersionUID = 3315676358210622635L;

    private ErrorType errorType;
    private final HttpStatus httpStatus;

    public APIException(HttpStatus httpStatus, ErrorType errorType) {
        this.errorType = errorType;
        this.httpStatus = httpStatus;
    }

    public APIException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public APIException(HttpStatus httpStatus, ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.httpStatus = httpStatus;
    }

    public APIException(HttpStatus httpStatus, ErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
        this.httpStatus = httpStatus;
    }

    public APIException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }


    public APIException(HttpStatus httpStatus, ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorType = errorType;
    }

    public APIException(HttpStatus httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }
}
