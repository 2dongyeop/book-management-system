package io.dongvelop.bookmanagementsystem.excepiton;

import io.dongvelop.bookmanagementsystem.common.Const;
import io.dongvelop.bookmanagementsystem.common.Utils;
import jakarta.validation.ConstraintDefinitionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @Value("${spring.web.locale:ko_KR}")
    private Locale locale;

    private final MessageSource messageSource;

    /**
     * Checked Exception 처리
     */
    @ExceptionHandler(value = APIException.class)
    public ResponseEntity<?> apiException(APIException e) {
        log.warn(e.getMessage(), e);
        return getExceptionResponse(e.getErrorType(), e.getMessage(), e.getHttpStatus());
    }

    /**
     * RequestParam 및 PathParam 에서 선언한 매개변수의 타입과 불일치시 발생하는 예외
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return getExceptionResponse(ErrorType.INVALID_INPUT, getCauseMessage(e.getCause()), HttpStatus.BAD_REQUEST);
    }

    /**
     * `@Valid` 등으로 검사된 `@NotBlank` 등과 같이 유효성 검사에 실패한 경우.
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException e) {
        return getExceptionResponse(ErrorType.INVALID_INPUT, getCauseDetailMessages(e), HttpStatus.BAD_REQUEST);
    }

    /**
     * `@ValidISVN10` 등과 같은 Custom Valid 애너테이션의 유효성 검증을 통과하지 못했을 경우
     */
    @ExceptionHandler(value = ConstraintDefinitionException.class)
    public ResponseEntity<?> constraintDefinitionException(ConstraintDefinitionException e) {
        return getExceptionResponse(ErrorType.INVALID_INPUT, getCauseMessage(e), HttpStatus.BAD_REQUEST);
    }

    /**
     * LocalDate 등의 시간 타입에 포맷이 잘못되어 요청이 왔을 경우.
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return getExceptionResponse(ErrorType.INVALID_INPUT, getCauseMessage(e), HttpStatus.BAD_REQUEST);
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

    private String getCauseMessage(final Throwable throwable) {
        String retValue = Const.STRING_EMPTY;

        if (throwable != null) {
            final Throwable childThrow = throwable.getCause();

            if (childThrow != null) {
                retValue = getCauseMessage(childThrow);
            } else {
                retValue = throwable.getMessage();
            }
        }

        return retValue;
    }

    private String getCauseDetailMessages(final BindException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                .toList().toString();
    }
}
