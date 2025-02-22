package io.dongvelop.bookmanagementsystem.excepiton;

import io.dongvelop.bookmanagementsystem.common.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @Value("${spring.mvc.locale:ko_KR}")
    private Locale locale;

    private final MessageSource messageSource;

    /**
     * Checked Exception 처리
     */
    @ExceptionHandler(value = APIException.class)
    public ResponseEntity<?> exceptionAPI(APIException e) {
        log.warn(e.getMessage(), e);
        return getExceptionResponse(e.getErrorType(), e.getMessage(), e.getHttpStatus());
    }

    /**
     * Unchecked Exception 처리
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        log.error(e.getMessage(), e);
        return getExceptionResponse(ErrorType.SERVER_ERROR, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> getExceptionResponse(final ErrorType errorType, final String message, final HttpStatus httpStatus) {
        return getExceptionResponse(errorType, message, httpStatus, locale);
    }

    private ResponseEntity<?> getExceptionResponse(final ErrorType errorType, final String message, final HttpStatus status, final Locale locale) {
        log.debug("errorType[{}], message[{}], status[{}], locale[{}]", errorType, message, status, locale);

        final ErrorResponse response = new ErrorResponse(errorType.getValue(), Utils.getLocaleMessage(messageSource, errorType.getValue(), message, locale));
        return new ResponseEntity<>(response, status);
    }
}
