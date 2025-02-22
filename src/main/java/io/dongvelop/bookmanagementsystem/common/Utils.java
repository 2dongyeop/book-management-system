package io.dongvelop.bookmanagementsystem.common;

import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.excepiton.ErrorType;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 유틸 클래스.
 */
public final class Utils {

    private Utils() {
    }

    /**
     * ISBN 유효성 검사 메서드
     */
    public static void validateISBN10(final String isbn) throws APIException {
        if (!StringUtils.hasText(isbn)) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_INPUT, "ISBN-10은 공백이거나 빈 값일 수 없습니다.");
        }

        if (isbn.chars().filter(ch -> ch == '-').count() != 2) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_INPUT, "ISBN-10은 하이픈(-)을 포함해야 합니다.");
        }

        final String cleanISBN = isbn.replaceAll("-", "");

        if (!cleanISBN.matches("\\d{10}")) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_INPUT, "ISBN-10은 10자리 숫자여야 합니다.");
        }

        final String prefix = cleanISBN.substring(0, 3);
        if (!isValidPrefix(prefix)) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_INPUT, "ISBN-10의 앞 세 자리는 100~900 사이여야 합니다.");
        }

        if (cleanISBN.charAt(9) != '0') {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_INPUT, "ISBN-10의 마지막 자리는 0이어야 합니다.");
        }
    }

    /**
     * ISBN 앞자리 유효성 검사 메서드
     */
    private static boolean isValidPrefix(String prefix) {
        try {
            final int prefixNum = Integer.parseInt(prefix);
            return prefixNum >= 100 && prefixNum <= 900;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * 해당 코드에 해당되는 다국어 메시지를 구한다.
     *
     * @param messageSource MessageSource 객체
     * @param code          코드 값
     * @param messages      properties 메시지에 정의할 값 배열
     * @param locale        언어 값
     * @return String 다국어 메시지
     */
    public static String getLocaleMessage(final MessageSource messageSource, final String code, final String messages, final Locale locale) {
        return getLocaleMessage(messageSource, code, new String[]{messages}, locale);
    }

    /**
     * 해당 코드에 해당되는 다국어 메시지를 구한다.
     *
     * @param messageSource MessageSource 객체
     * @param code          코드 값
     * @param messages      properties 메시지에 정의할 값 배열
     * @param locale        언어 값
     * @return String 다국어 메시지
     */
    public static String getLocaleMessage(final MessageSource messageSource, final String code, final String[] messages, final Locale locale) {

        String retValue;

        try {
            retValue = messageSource.getMessage(code, messages, locale);
        } catch (NoSuchMessageException e) {
            retValue = "check locale message. invalide code [" + code + "]";
        }

        return retValue;
    }
}
