package io.dongvelop.bookmanagementsystem.common;

import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description ISBN-10 포맷을 검증하는 Validator
 */
public class ISBN10Validator implements ConstraintValidator<ValidISBN10, String> {

    @Override
    public void initialize(ValidISBN10 constraintAnnotation) {
    }

    /**
     * 포맷 검증 메서드. 검증 로직은 Book Entity 로직에서도 사용되어 Utils 클래스에 작성하여 공통으로 호출 사용.
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Utils.validateISBN10(s);
            return true;
        } catch (APIException e) {
            return false;
        }
    }
}

