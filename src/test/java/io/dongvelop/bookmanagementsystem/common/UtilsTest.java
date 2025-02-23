package io.dongvelop.bookmanagementsystem.common;

import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.excepiton.ErrorType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

class UtilsTest {

    @Nested
    @DisplayName("ISBN 유효성 검증")
    class validateISBN10 {

        @Test
        @DisplayName("ISBN 값이 공백이면 검증에 실패한다.")
        void invalidISBN1_1() {

            // given
            final String invalidISBN = " ";

            // when
            final APIException apiException = Assertions.assertThrows(
                    APIException.class,
                    () -> new Book("title", null, invalidISBN, LocalDate.now(), new Author("name", "email@naver.com"))
            );

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.INVALID_INPUT);
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(apiException.getMessage().equals("ISBN-10은 공백이거나 빈 값일 수 없습니다.")).isTrue();
            });
        }

        @Test
        @DisplayName("ISBN이 빈 값이면 검증에 실패한다.")
        void invalidISBN1_2() {

            // given
            final String invalidISBN = "";

            // when
            final APIException apiException = Assertions.assertThrows(
                    APIException.class,
                    () -> new Book("title", null, invalidISBN, LocalDate.now(), new Author("name", "email@naver.com"))
            );

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.INVALID_INPUT);
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(apiException.getMessage().equals("ISBN-10은 공백이거나 빈 값일 수 없습니다.")).isTrue();
            });
        }

        @Test
        @DisplayName("ISBN이 빈 값이면 검증에 실패한다.")
        void invalidISBN1_3() {

            // given
            final String invalidISBN = "1005689010";

            // when
            final APIException apiException = Assertions.assertThrows(
                    APIException.class,
                    () -> new Book("title", null, invalidISBN, LocalDate.now(), new Author("name", "email@naver.com"))
            );

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.INVALID_INPUT);
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(apiException.getMessage().equals("ISBN-10은 하이픈(-)을 포함해야 합니다.")).isTrue();
            });
        }

        @Test
        @DisplayName("ISBN의 총 자리수는 10자리여야 한다.")
        void invalidISBN2() {

            // given
            final String invalidISBN = "99-568901-0";

            // when
            final APIException apiException = Assertions.assertThrows(
                    APIException.class,
                    () -> new Book("title", null, invalidISBN, LocalDate.now(), new Author("name", "email@naver.com"))
            );

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.INVALID_INPUT);
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(apiException.getMessage().equals("ISBN-10은 10자리 숫자여야 합니다.")).isTrue();
            });
        }

        @Test
        @DisplayName("ISBN의 첫 3자리(국가/언어 식별 번호)는 100~900 사이여야 한다.")
        void invalidISBN3() {

            // given
            final String invalidISBN = "099-568901-0";

            // when
            final APIException apiException = Assertions.assertThrows(
                    APIException.class,
                    () -> new Book("title", null, invalidISBN, LocalDate.now(), new Author("name", "email@naver.com"))
            );

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.INVALID_INPUT);
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(apiException.getMessage().equals("ISBN-10의 앞 세 자리는 100~900 사이여야 합니다.")).isTrue();
            });
        }

        @Test
        @DisplayName("ISBN-10의 마지막 자리는 0이어야 한다.")
        void invalidISBN4() {

            // given
            final String invalidISBN = "100-568901-1";

            // when
            final APIException apiException = Assertions.assertThrows(
                    APIException.class,
                    () -> new Book("title", null, invalidISBN, LocalDate.now(), new Author("name", "email@naver.com"))
            );

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.INVALID_INPUT);
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(apiException.getMessage().equals("ISBN-10의 마지막 자리는 0이어야 합니다.")).isTrue();
            });
        }
    }
}
