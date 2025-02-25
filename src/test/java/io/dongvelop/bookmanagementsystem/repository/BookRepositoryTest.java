package io.dongvelop.bookmanagementsystem.repository;

import autoparams.AutoSource;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import io.dongvelop.bookmanagementsystem.exception.APIException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Nested
    @DisplayName("ISBN 존재 여부 검증 테스트")
    class ExistByIsbn {

        @AutoSource
        @ParameterizedTest
        @DisplayName("이미 존재하는 ISBN일 경우, 반환 값이 참으로 나온다.")
        void resultTrue(Author author) throws APIException {

            // given
            final Author savedAuthor = authorRepository.save(author);

            final String isbn = "123-456789-0";
            bookRepository.save(new Book("title", "des", isbn, LocalDate.now(), savedAuthor));

            // when
            boolean result = bookRepository.existsByIsbn(isbn);

            // then
            Assertions.assertThat(result).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 ISBN일 경우, 반환 값이 거짓으로 나온다.")
        void resultFalse() {

            // given

            // when
            boolean result = bookRepository.existsByIsbn("123-456789-0");

            // then
            Assertions.assertThat(result).isFalse();
        }
    }
}