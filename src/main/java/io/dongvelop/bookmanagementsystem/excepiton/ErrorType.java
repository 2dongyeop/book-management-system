package io.dongvelop.bookmanagementsystem.excepiton;

import lombok.Getter;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 공통 정의 에러 타입.
 */
@Getter
public enum ErrorType {
    /**
     * 필수 입력값이 누락되었습니다.
     */
    REQUIRED_INPUT("100"),

    /**
     * 입력값이 올바르지 않습니다.(사이즈, 타입)
     */
    INVALID_INPUT("101"),

    /**
     * PARAM/QUERY/JSON 데이터 포맷 에러입니다.
     */
    PARAM_FORMAT("102"),

    /**
     * 이미 데이터가 존재합니다.
     */
    EXIST_DATA("103"),

    /**
     * 데이터가 존재하지 않습니다.
     */
    NOT_EXIST_DATA("104"),

    /**
     * 직접 정의하는 메시지.
     */
    CUSTOM("777"),

    /**
     * 인가 실패.다국어 메시지 없음
     */
    UNAUTHORIZED("888"),

    /**
     * 내부 서버 에러입니다.
     */
    SERVER_ERROR("999");

    private final String value;

    ErrorType(final String value) {
        this.value = value;
    }
}
