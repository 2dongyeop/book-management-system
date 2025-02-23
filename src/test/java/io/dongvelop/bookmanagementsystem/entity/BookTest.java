package io.dongvelop.bookmanagementsystem.entity;

import autoparams.AutoSource;
import io.dongvelop.bookmanagementsystem.common.Const;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateBookRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.time.LocalDate;

class BookTest {

    @Nested
    @DisplayName("직접 선언한 Getter에 대한 기본값 처리 테스트")
    class CustomGetter {

        @Test
        @DisplayName("description이 null일 경우, 빈 값으로 반환된다.")
        void getDescription() {

            // given
            final Book book = new Book();

            // when
            final String result = book.getDescription();

            // then
            Assertions.assertThat(result).isEqualTo(Const.STRING_EMPTY);
        }

        @Test
        @DisplayName("publicationDate이 null일 경우, 오늘 일자가 반환된다.")
        void getPublicationDate() {

            // given
            final Book book = new Book();

            // when
            final LocalDate publicationDate = book.getPublicationDate();

            // then
            Assertions.assertThat(publicationDate).isEqualTo(LocalDate.now());
        }
    }

    @Nested
    @DisplayName("도서 수정 로직 테스트")
    class UpdateBook {

        @AutoSource
        @ParameterizedTest
        @DisplayName("요청 값이 유효할 경우, 값이 변경된다.")
        void success(final UpdateBookRequest request) {

            // given
            final Book book = new Book();

            // when
            book.update(request);

            // then
            Assertions.assertThat(book.getTitle()).isEqualTo(request.title());
            Assertions.assertThat(book.getDescription()).isEqualTo(request.description());
            Assertions.assertThat(book.getPublicationDate()).isEqualTo(request.publicationDate());
        }

        @Test
        @DisplayName("요청 값이 유효하지 않을 경우, 값이 변경되지 않는다.")
        void fail() {

            // given
            final Book book = new Book();
            final UpdateBookRequest request = new UpdateBookRequest(" ", " ", null);

            // when
            book.update(request);

            // then
            Assertions.assertThat(book.getTitle()).isNotEqualTo(request.title());
            Assertions.assertThat(book.getDescription()).isNotEqualTo(request.description());
            Assertions.assertThat(book.getPublicationDate()).isNotEqualTo(request.publicationDate());
        }
    }
}