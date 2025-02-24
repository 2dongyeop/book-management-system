package io.dongvelop.bookmanagementsystem.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 24
 * @description `Aspect`를 이용해 Controller 내부의 공통 로깅 로직 분리
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final HttpServletRequest request;

    /**
     * `endpoint` 패키지 내에 `@RestController` 애너테이션이 붙은 클래스에 메서드가 "실행되기 전"에 적용.
     */
    @Before("execution(* io.dongvelop.bookmanagementsystem.endpoint.*.*(..)) && @within(org.springframework.web.bind.annotation.RestController)")
    public void logRequest(final JoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String methodName = signature.getMethod().getName();
        final Object[] args = joinPoint.getArgs();

        log.info("Request [{} {}] - Method: {}, Args: {}", request.getMethod(), request.getRequestURI(), methodName, Arrays.toString(args));
    }
}
