package io.dongvelop.bookmanagementsystem.common;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description
 */

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description ISBN 포맷을 검증하기 위한 애너테이션
 */
@Constraint(validatedBy = ISBN10Validator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidISBN10 {
    String message() default "유효하지 않은 ISBN-10 포맷입니다. 예시 : 123-456789-0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
